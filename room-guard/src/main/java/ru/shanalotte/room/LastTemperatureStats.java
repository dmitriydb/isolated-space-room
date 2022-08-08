package ru.shanalotte.room;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;

@Getter
public class LastTemperatureStats {
  private AtomicInteger lastVector = new AtomicInteger(0);
  private AtomicInteger lastChangeSpeed = new AtomicInteger(0);
  private AtomicInteger lastTemperature = new AtomicInteger(0);
}
