package ru.shanalotte.temperature.generator;

import lombok.Data;

@Data
public class TemperatureState {
  private final TemperatureVector vector;
  private final int temperature;
  private final int changeSpeed;
}
