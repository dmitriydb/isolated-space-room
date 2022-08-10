package ru.shanalotte.room;

import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import ru.shanalotte.room.rest.MainRestController;

@Slf4j
@SpringBootApplication
@ComponentScan("ru.shanalotte")
public class RoomGuardLauncher implements CommandLineRunner {

  @Autowired
  private MainRestController mainRestController;

  private static Room room;
  private static LastTemperatureStats lastTemperatureStats;

  public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, IOException {
    lastTemperatureStats = new LastTemperatureStats();
    room = new Room();
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
   // Endpoint endpoint = Endpoint.publish("http://localhost:10005/room/state", webService);
   // log.warn("ENDPOINT IS {}", endpoint.isPublished());
    SpringApplication.run(RoomGuardLauncher.class, args);
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

  @Override
  public void run(String... args) throws Exception {
     mainRestController.setRoom(room);
     mainRestController.setLastTemperatureStats(lastTemperatureStats);
  }
}
