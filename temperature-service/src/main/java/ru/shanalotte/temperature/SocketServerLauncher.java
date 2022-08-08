package ru.shanalotte.temperature;

import java.io.IOException;
import ru.shanalotte.temperature.generator.CollectionTemperatureStateListener;
import ru.shanalotte.temperature.generator.SimpleTemperatureGenerator;
import ru.shanalotte.temperature.generator.TemperatureGenerator;
import ru.shanalotte.temperature.generator.TemperatureStateListener;
import ru.shanalotte.temperature.socket.TemperatureSocketServer;

public class SocketServerLauncher {

  public static void main(String[] args) throws IOException {
    TemperatureGenerator temperatureGenerator = new SimpleTemperatureGenerator();
    TemperatureSocketServer temperatureSocketServer = new TemperatureSocketServer(temperatureGenerator);
    CollectionTemperatureStateListener listener = new CollectionTemperatureStateListener();
    temperatureGenerator.addListener(temperatureSocketServer);
    temperatureGenerator.addListener(listener);
    temperatureGenerator.start();
    temperatureSocketServer.start();
  }
}
