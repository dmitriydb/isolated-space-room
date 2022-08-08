package ru.shanalotte.room;

public class RoomGuardLauncher {

  public static void main(String[] args) {
    LastTemperatureStats lastTemperatureStats = new LastTemperatureStats();
    Room room = new Room();
    ConnectionMonitor connectionMonitor = new ConnectionMonitor(room);
    room.attachConnectionMonitor(connectionMonitor);
    connectionMonitor.start();
    for (int i = 0; i < 4; i++) {
      new TemperatureConsumer(room, lastTemperatureStats).start();
    }
  }
}
