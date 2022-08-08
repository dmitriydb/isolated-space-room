package ru.shanalotte.temperature.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import ru.shanalotte.config.TemperatureGeneratorConfig;
import ru.shanalotte.constants.TemperatureConstants;

@Slf4j
public class SimpleTemperatureGenerator implements TemperatureGenerator, SimpleTemperatureGeneratorMBean {

  private AtomicInteger currentTemperature = new AtomicInteger(0);
  private AtomicInteger temperatureChangeSpeed = new AtomicInteger(0);
  private volatile TemperatureVector currentVector = TemperatureVector.INCREASING;
  private List<TemperatureStateListener> listeners = new ArrayList<>();
  private ArrayBlockingQueue<TemperatureState> listenerEvents = new ArrayBlockingQueue<>(1000);
  private AtomicInteger goal = new AtomicInteger(0);
  public SimpleTemperatureGenerator() {

  }

  public void start() {
    chooseInitialVector();
    chooseRandomTemperatureChangingSpeed();
    sendState();
    new VectorChanger().start();
    new TemperatureRefresher().start();
    new ListenerNotifier().start();
  }

  @Override
  public void addListener(TemperatureStateListener listener) {
    listeners.add(listener);
  }

  private void updateTemperature() {
    int delta = currentVector == TemperatureVector.INCREASING ? temperatureChangeSpeed.get() :
        -temperatureChangeSpeed.get();
    currentTemperature.set(delta + currentTemperature.get());
    sendState();
  }

  public int getCurrentTemperature() {
    return currentTemperature.get();
  }

  @Override
  public TemperatureVector getCurrentTemperatureVector() {
    return currentVector;
  }

  private void changeVector() {
    if (currentVector == TemperatureVector.INCREASING) {
      currentVector = TemperatureVector.DECREASING;
    } else {
      currentVector = TemperatureVector.INCREASING;
    }
    sendState();
  }

  private void chooseInitialVector() {
    if (ThreadLocalRandom.current().nextInt(2) == 0) {
      currentVector = TemperatureVector.INCREASING;
    } else {
      currentVector = TemperatureVector.DECREASING;
    }
    setNewGoal();
  }

  public void setNewGoal() {
    if (currentVector == TemperatureVector.INCREASING) {
      goal.set(ThreadLocalRandom.current().nextInt(TemperatureConstants.MAX_DOABLE_TEMPERATURE));
    } else
    {
      goal.set(-ThreadLocalRandom.current().nextInt(-TemperatureConstants.MIN_DOABLE_TEMPERATURE));
    }
    log.info("NEW GOAL IS {}", goal.get());
  }

  public boolean isGoalReached() {
    if (currentVector == TemperatureVector.INCREASING) {
      return currentTemperature.get() >= goal.get();
    } else
    {
      return currentTemperature.get() <= goal.get();
    }
  }

  private void chooseRandomTemperatureChangingSpeed() {
    temperatureChangeSpeed.set(ThreadLocalRandom.current().nextInt(TemperatureGeneratorConfig.MAX_TEMPERATURE_CHANGE_SPEED) + 1);
  }

  private class ListenerNotifier extends Thread {

    public ListenerNotifier() {
      this.setDaemon(true);
    }

    public void run() {
      while (true) {
        TemperatureState nextEvent = null;
        while ((nextEvent = listenerEvents.poll()) != null) {
          for (TemperatureStateListener listener : listeners) {
            listener.getNewState(nextEvent);
          }
        }
      }
    }
  }

  private class TemperatureRefresher extends Thread {
    public void run() {
      try {
        while (true) {
          Thread.sleep(TemperatureGeneratorConfig.TEMPERATURE_CHANGE_TIMEOUT_MS);
          updateTemperature();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private class VectorChanger extends Thread {
    public void run() {
      try {
        while (true) {
          Thread.sleep(TemperatureGeneratorConfig.VECTOR_CHANGE_DELAY_MS);
          if (isGoalReached()) {
            changeVector();
            setNewGoal();
            chooseRandomTemperatureChangingSpeed();
            sendState();
          }
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void setCurrentTemperature(int temperature) {
    this.currentTemperature.set(temperature);
  }

  private void sendState() {
    listenerEvents.add(currentState());
  }

  private TemperatureState currentState() {
    return new TemperatureState(currentVector, currentTemperature.get(), temperatureChangeSpeed.get());
  }
}
