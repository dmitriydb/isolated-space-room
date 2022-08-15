package ru.shanalotte.temperature.generator;

import java.util.List;
import ru.shanalotte.schemas.TemperatureState;

public interface TemperatureStateListener {
  void getNewState(TemperatureState state);
  List<TemperatureState> recordedEvents();
}
