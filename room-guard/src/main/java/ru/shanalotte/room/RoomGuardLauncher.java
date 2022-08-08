package ru.shanalotte.room;

public class RoomGuardLauncher {

  public static void main(String[] args) {
    Room room = new Room();
    for (int i = 0; i < 10; i++) {
      new TemperatureConsumer(room).start();
    }
  }
}
