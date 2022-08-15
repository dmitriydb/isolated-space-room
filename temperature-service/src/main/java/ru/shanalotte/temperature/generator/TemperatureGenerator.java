package ru.shanalotte.temperature.generator;

import ru.shanalotte.schemas.TemperatureVector;

public interface TemperatureGenerator {
  void addListener(TemperatureStateListener listener);

  int getCurrentTemperature();
  TemperatureVector getCurrentTemperatureVector();
  public void start();
}
