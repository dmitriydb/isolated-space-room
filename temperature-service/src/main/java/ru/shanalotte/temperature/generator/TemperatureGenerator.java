package ru.shanalotte.temperature.generator;

public interface TemperatureGenerator {
  void addListener(TemperatureStateListener listener);

  int getCurrentTemperature();
  TemperatureVector getCurrentTemperatureVector();
  public void start();
}
