package ru.shanalotte.schemas;

import lombok.Data;
import ru.shanalotte.schemas.TemperatureStateRecord;

@Data
public class TemperatureState {
  private final TemperatureVector vector;
  private final int temperature;
  private final int changeSpeed;

  public TemperatureStateRecord toRecord() {
    return new TemperatureStateRecord(vector.name(), temperature, changeSpeed);
  }
}
