package ru.shanalotte.room.rest.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomStatsDto {

  private @NonNull LocalDateTime ts;
  private @NonNull int temperature;
  private @NonNull int vector;
  private @NonNull int speed;
  private @NonNull String roomState;
}
