package ru.shanalotte.temperature.sensor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

public class Sensor {

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
    Properties properties = new Properties();
    properties.load(Sensor.class.getClassLoader().getResourceAsStream("application-" + profile + ".properties"));
    serverHost = properties.getProperty("temperature.server.host");
    serverPort = Integer.parseInt(properties.getProperty("temperature.server.port"));
  }

  @SneakyThrows
  public void startSensing() {
    @Cleanup Socket s = new Socket(serverHost, serverPort);
    @Cleanup BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    in.lines().forEach(recordedEvents::add);
  }
}
