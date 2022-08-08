package ru.shanalotte.temperature.sensor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sensor {

  private SensorProducer sensorProducer;
  @Getter
  private final List<String> recordedEvents = new ArrayList<>();
  @Getter
  private String serverHost;
  @Getter
  private int serverPort;

  public Sensor() {
    loadProperties();
  }

  public Sensor(String profile) {
    loadProperties(profile);
  }

  private void loadProperties() {
    if (System.getenv().containsKey("PRODUCTION")) {
      loadProperties("production");
    } else {
      loadProperties("dev");
    }
  }

  @SneakyThrows
  private void loadProperties(String profile) {
    log.info("Profile active: {}. Loading properties...", profile);
    Properties properties = new Properties();
    properties.load(Sensor.class.getClassLoader().getResourceAsStream("application-" + profile + ".properties"));
    serverHost = properties.getProperty("temperature.server.host");
    serverPort = Integer.parseInt(properties.getProperty("temperature.server.port"));
    log.info("Done!");
  }

  @SneakyThrows
  public void startSensing() {
    @Cleanup Socket s = new Socket(serverHost, serverPort);
    @Cleanup BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    in.lines().forEach(line -> {
      if (doesNotDuplicate(line)) {
        if (sensorProducer != null) {
          sensorProducer.sendRecord(line);
        }
        recordedEvents.add(line);
      }
    });
  }

  private boolean doesNotDuplicate(String line) {
    if (recordedEvents.size() == 0) {
      return true;
    }
    return !recordedEvents.get(recordedEvents.size() - 1).equals(line);
  }

  public void attachProducer(SensorProducer sensorProducer) {
    this.sensorProducer = sensorProducer;
  }
}
