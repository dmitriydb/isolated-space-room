package ru.shanalotte.temperature.generator;

import java.util.List;

public interface TemperatureStateListener {
  void getNewState(TemperatureState state);
  List<TemperatureState> recordedEvents();
}
