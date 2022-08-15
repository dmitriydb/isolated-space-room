package ru.shanalotte.temperature.generator;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import ru.shanalotte.schemas.TemperatureState;

@Slf4j
public class CollectionTemperatureStateListener implements TemperatureStateListener{

  private final List<TemperatureState> recordedEvents = new ArrayList<>();
  @Override
  public void getNewState(TemperatureState state) {
    log.debug(state.toString());
    recordedEvents.add(state);
  }

  @Override
  public List<TemperatureState> recordedEvents() {
    return recordedEvents;
  }
}
