package ru.shanalotte.room;

import java.lang.management.ManagementFactory;
import java.util.Properties;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoomGuardLauncher {

  public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
    LastTemperatureStats lastTemperatureStats = new LastTemperatureStats();
    Room room = new Room();
    ConnectionMonitor connectionMonitor = new ConnectionMonitor(room);
    room.attachConnectionMonitor(connectionMonitor);
    connectionMonitor.start();
    String bootstrapUrl = bootstrapURL();

    for (int i = 0; i < 4; i++) {
      new TemperatureConsumer(room, lastTemperatureStats ,bootstrapUrl).start();
    }

    MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    ObjectName objectName = new ObjectName("ru.shanalotte.room", "room", "connectionmonitor");
    ObjectName objectName2 = new ObjectName("ru.shanalotte.room", "room", "lasttemperature");
    ObjectName objectName3 = new ObjectName("ru.shanalotte.room", "room", "room");
    server.registerMBean(lastTemperatureStats, objectName2);
    server.registerMBean(room, objectName3);
    server.registerMBean(connectionMonitor, objectName);
  }

  private static String bootstrapURL() {
    if (System.getenv().containsKey("PRODUCTION")) {
      return bootstrapURL("production");
    } else {
      return bootstrapURL("dev");
    }
  }

  @SneakyThrows
  private static String bootstrapURL(String profile) {
    log.info("Profile active: {}. Loading properties...", profile);
    Properties properties = new Properties();
    properties.load(RoomGuardLauncher.class.getClassLoader().getResourceAsStream("application-" + profile + ".properties"));
    String bootstrapServerHost = properties.getProperty("bootstrap.server.host");
    int bootstrapServerPort = Integer.parseInt(properties.getProperty("bootstrap.server.port"));
    return bootstrapServerHost + ":" + bootstrapServerPort;
  }
}
