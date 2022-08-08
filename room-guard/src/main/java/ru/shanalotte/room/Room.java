package ru.shanalotte.room;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Room {

  public AtomicInteger status = new AtomicInteger();

  public String state() {
    return status.get() == 0 ? "closed" : "open";
  }

  public synchronized void closeRoom() {
    this.status.set(0);
    log.info("[*** ROOM IS CLOSED ***]");
  }

  public synchronized void openRoom() {
    this.status.set(1);
    log.info("[*** ROOM IS OPENED ***]");
  }

  public boolean isClosed() {
    return status.get() == 0;
  }
}
