package ru.shanalotte.room;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Room implements RoomMBean{

  @Getter
  public AtomicInteger status = new AtomicInteger(0);
  private ConnectionMonitor connectionMonitor;

  public String state() {
    return status.get() == 0 ? "closed" : "open";
  }

  public synchronized void closeRoom() {
    this.status.set(0);
    connectionMonitor.updateTime();
    log.info("[*** ROOM IS CLOSED ***]");
  }

  public synchronized void openRoom() {
    this.status.set(1);
    connectionMonitor.updateTime();
    log.info("[*** ROOM IS OPENED ***]");
  }

  public boolean isClosed() {
    return status.get() == 0;
  }

  public void attachConnectionMonitor(ConnectionMonitor connectionMonitor) {
    this.connectionMonitor = connectionMonitor;
  }
}
