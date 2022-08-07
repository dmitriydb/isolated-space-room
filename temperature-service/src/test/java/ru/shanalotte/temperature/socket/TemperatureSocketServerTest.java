package ru.shanalotte.temperature.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Cleanup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import ru.shanalotte.temperature.TemperatureSocketServerConfig;
import ru.shanalotte.temperature.generator.CollectionTemperatureStateListener;
import ru.shanalotte.temperature.generator.SimpleTemperatureGenerator;
import ru.shanalotte.temperature.generator.TemperatureGenerator;
import ru.shanalotte.temperature.generator.TemperatureState;
import ru.shanalotte.temperature.generator.TemperatureStateListener;

class TemperatureSocketServerTest {

  @Test
  public void socket_ShouldGiveCurrentTemperature() throws IOException, InterruptedException {
    TemperatureGenerator temperatureGenerator = new SimpleTemperatureGenerator();
    TemperatureStateListener listener = new CollectionTemperatureStateListener();
    TemperatureSocketServer temperatureSocketServer = new TemperatureSocketServer(9999, temperatureGenerator);
    temperatureGenerator.addListener(temperatureSocketServer);
    temperatureGenerator.addListener(listener);
    temperatureGenerator.start();
    temperatureSocketServer.start();
    @Cleanup Socket s = new Socket("localhost", 9999);
    @Cleanup BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    int maxEventsToTest = 0;
    List<String> recordedSocketEvents = new ArrayList<>();
    String line = null;
    while ((line = in.readLine()) != null) {
      maxEventsToTest++;
      recordedSocketEvents.add(line);
      if (maxEventsToTest >= 10) {
        break;
      }
    }
    List<String> actualEvents = listener.recordedEvents().stream().map(TemperatureState::toString).collect(Collectors.toList());
    assertThat(actualEvents).containsAll(recordedSocketEvents.subList(0, recordedSocketEvents.size() - 1));
  }


  @Test
  public void multipleSocketsSupport() throws IOException, InterruptedException {
    TemperatureGenerator temperatureGenerator = new SimpleTemperatureGenerator();
    TemperatureStateListener listener = new CollectionTemperatureStateListener();
    TemperatureSocketServer temperatureSocketServer = new TemperatureSocketServer(9998, temperatureGenerator);
    temperatureGenerator.addListener(temperatureSocketServer);
    temperatureGenerator.addListener(listener);
    temperatureGenerator.start();
    temperatureSocketServer.start();
    Runnable r1 = () -> {
      try {
        @Cleanup Socket s = new Socket("localhost", 9998);
        @Cleanup BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        List<String> recordedEvents = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
          recordedEvents.add(in.readLine());
        }
        assertThat(listener.recordedEvents().stream().map(TemperatureState::toString)
            .collect(Collectors.toList())).containsAll(recordedEvents.subList(0, recordedEvents.size() - 1));

      } catch (Exception ex) {
        ex.printStackTrace();
      }
    };
    Runnable r2 = () -> {
      try {
        @Cleanup Socket s = new Socket("localhost", 9998);
        @Cleanup BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        List<String> recordedEvents = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
          recordedEvents.add(in.readLine());
        }
        assertThat(listener.recordedEvents().stream().map(TemperatureState::toString)
            .collect(Collectors.toList())).containsAll(recordedEvents.subList(0, recordedEvents.size() - 1));

      } catch (Exception ex) {
        ex.printStackTrace();
      }
    };
    Runnable r3 = () -> {
      try {
        @Cleanup Socket s = new Socket("localhost", 9998);
        @Cleanup BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        List<String> recordedEvents = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
          recordedEvents.add(in.readLine());
        }
        assertThat(listener.recordedEvents().stream().map(TemperatureState::toString)
            .collect(Collectors.toList())).containsAll(recordedEvents.subList(0, recordedEvents.size() - 1));
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    };
    new Thread(r1).start();
    new Thread(r2).start();
    new Thread(r3).start();
    Thread.sleep(2000L);
  }

  @Test
  public void shouldRunOnDefaultPortIfTSocketVariableMissing() {
    if (System.getenv(TemperatureSocketServerConfig.PORT_ENV_VARIABLE) != null) {
      return;
    }
    TemperatureSocketServer socketServer = new TemperatureSocketServer(new SimpleTemperatureGenerator());
    assertThat(String.valueOf(socketServer.getPort()))
        .isEqualTo(TemperatureSocketServerConfig.DEFAULT_PORT);
  }

  @Test
  public void shouldRunOnTSocketServerVariableIfSet() throws InterruptedException {
    if (System.getenv(TemperatureSocketServerConfig.PORT_ENV_VARIABLE) == null) {
      return;
    }
    TemperatureSocketServer socketServer = new TemperatureSocketServer(new SimpleTemperatureGenerator());
    assertThat(String.valueOf(socketServer.getPort()))
        .isEqualTo(System.getenv(TemperatureSocketServerConfig.PORT_ENV_VARIABLE));
  }

}