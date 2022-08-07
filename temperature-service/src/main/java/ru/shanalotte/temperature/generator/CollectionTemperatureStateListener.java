package ru.shanalotte.temperature.generator;

import java.util.ArrayList;
import java.util.List;

public class CollectionTemperatureStateListener implements TemperatureStateListener{

  private final List<TemperatureState> recordedEvents = new ArrayList<>();
  @Override
  public void getNewState(TemperatureState state) {
    recordedEvents.add(state);
  }

  @Override
  public List<TemperatureState> recordedEvents() {
    return recordedEvents;
  }
}
