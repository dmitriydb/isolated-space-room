package ru.shanalotte.room.rest;

import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.shanalotte.room.LastTemperatureStats;
import ru.shanalotte.room.Room;
import ru.shanalotte.room.rest.dto.RoomStatsDto;

@RestController
public class MainRestController {

  private LastTemperatureStats lastTemperatureStats;
  private Room room;

  @CrossOrigin(origins = "*")
  @GetMapping(value = "/stats", produces = "application/json")
  public @ResponseBody RoomStatsDto getRoomStats() {
    if (lastTemperatureStats == null) {
      return new RoomStatsDto(LocalDateTime.now(), 0, 0, 0, "closed");
    }
    return new RoomStatsDto(LocalDateTime.now(), lastTemperatureStats.getTemperature(),
        lastTemperatureStats.getVector(),
        lastTemperatureStats.getChangeSpeed(),
        room.state());
  }

  public void setLastTemperatureStats(LastTemperatureStats lastTemperatureStats) {
    this.lastTemperatureStats = lastTemperatureStats;
  }

  public void setRoom(Room room) {
    this.room = room;
  }
}
