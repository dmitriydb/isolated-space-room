package ru.shanalotte.schemas;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TemperatureStateRecord {
  private @NonNull String vector;
  private @NonNull int currentTemperature;
  private @NonNull int changeSpeed;
}
