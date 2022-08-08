package ru.shanalotte.room;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;

@Getter
public class LastTemperatureStats implements LastTemperatureStatsMBean{
  private AtomicInteger lastVector = new AtomicInteger(0);
  private AtomicInteger lastChangeSpeed = new AtomicInteger(0);
  private AtomicInteger lastTemperature = new AtomicInteger(0);

  @Override
  public int getTemperature() {
    return lastTemperature.get();
  }

  @Override
  public int getChangeSpeed() {
    return lastChangeSpeed.get();
  }

  @Override
  public int getVector() {
    return lastVector.get();
  }
}
