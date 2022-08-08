package ru.shanalotte.schemas;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
public class TemperatureStateRecord {
  private @NonNull String vector;
  private @NonNull int currentTemperature;
  private @NonNull int changeSpeed;
}
