package ru.shanalotte.temperature.generator;

public interface SimpleTemperatureGeneratorMBean {
  void setCurrentTemperature(int temperature);
  int getCurrentTemperature();
}
