package ru.shanalotte.room;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;

@Getter
public class LastTemperatureStats {
  private AtomicInteger lastVector;
  private AtomicInteger lastChangeSpeed;
  private AtomicInteger lastTemperature;
}
