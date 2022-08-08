package ru.shanalotte.room;

import java.lang.management.ManagementFactory;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class RoomGuardLauncher {

  public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
    LastTemperatureStats lastTemperatureStats = new LastTemperatureStats();
    Room room = new Room();
    ConnectionMonitor connectionMonitor = new ConnectionMonitor(room);
    room.attachConnectionMonitor(connectionMonitor);
    connectionMonitor.start();
    for (int i = 0; i < 4; i++) {
      new TemperatureConsumer(room, lastTemperatureStats).start();
    }

    MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    ObjectName objectName = new ObjectName("ru.shanalotte.room", "room", "connectionmonitor");
    ObjectName objectName2 = new ObjectName("ru.shanalotte.room", "room", "lasttemperature");
    ObjectName objectName3 = new ObjectName("ru.shanalotte.room", "room", "room");
    server.registerMBean(lastTemperatureStats, objectName2);
    server.registerMBean(room, objectName3);
    server.registerMBean(connectionMonitor, objectName);
  }
}
