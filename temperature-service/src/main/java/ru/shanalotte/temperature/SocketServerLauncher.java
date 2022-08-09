package ru.shanalotte.temperature;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import ru.shanalotte.temperature.generator.CollectionTemperatureStateListener;
import ru.shanalotte.temperature.generator.SimpleTemperatureGenerator;
import ru.shanalotte.temperature.generator.TemperatureGenerator;
import ru.shanalotte.temperature.socket.TemperatureSocketServer;

public class SocketServerLauncher {

  public static void main(String[] args) throws IOException, MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
    TemperatureGenerator temperatureGenerator = new SimpleTemperatureGenerator();
    TemperatureSocketServer temperatureSocketServer = new TemperatureSocketServer(temperatureGenerator);
    CollectionTemperatureStateListener listener = new CollectionTemperatureStateListener();
    temperatureGenerator.addListener(temperatureSocketServer);
    temperatureGenerator.addListener(listener);
    temperatureGenerator.start();
    temperatureSocketServer.start();

    ObjectName objectName = new ObjectName("ru.shanalotte.temperature:type=temperaturecontrol,name=temperature");
    MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    mBeanServer.registerMBean(temperatureGenerator, objectName);
  }
}
