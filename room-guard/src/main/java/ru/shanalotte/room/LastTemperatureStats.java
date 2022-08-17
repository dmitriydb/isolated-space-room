package ru.shanalotte.room;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class LastTemperatureStats implements LastTemperatureStatsMBean{
  private AtomicInteger lastVector = new AtomicInteger(0);
  private AtomicInteger lastChangeSpeed = new AtomicInteger(0);
  private AtomicInteger lastTemperature = new AtomicInteger(0);

  public LastTemperatureStats(int lastVector, int lastChangeSpeed, int lastTemperature) {
    this.lastVector = new AtomicInteger(lastVector);
    this.lastChangeSpeed = new AtomicInteger(lastChangeSpeed);
    this.lastTemperature = new AtomicInteger(lastTemperature);
  }

  @Override
  public int getTemperature() {
    return lastTemperature.get();
  }

  @Override
  public int getChangeSpeed() {
    return lastChangeSpeed.get();
  }

  @Override
  public int getVector() {
    return lastVector.get();
  }

  public void setTemperature(int temperature) {
    lastTemperature.set(temperature);
  }

  public void setSpeed(int speed) {
    lastChangeSpeed.set(speed);
  }

  public void setVector(int vector) {
    lastVector.set(vector);
  }

}
