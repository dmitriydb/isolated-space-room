package ru.shanalotte.room;

import java.time.Instant;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.shanalotte.config.TemperatureGeneratorConfig;

@RequiredArgsConstructor
@Slf4j
public class ConnectionMonitor extends Thread implements ConnectionMonitorMBean{
  private volatile Instant lastUpdateTime = Instant.now();
  private final Room room;

  public void updateTime() {
    this.lastUpdateTime = Instant.now();
  }

  @Override
  public String lastUpdateTime() {
    return lastUpdateTime.toString();
  }

  @SneakyThrows
  public void run() {
    while (true) {
      synchronized (this) {
        this.wait(TemperatureGeneratorConfig.TEMPERATURE_CHANGE_TIMEOUT_MS);
      }
      long before = lastUpdateTime.toEpochMilli();
      long after = Instant.now().toEpochMilli();
      if (after - before >= TemperatureGeneratorConfig.TEMPERATURE_CHANGE_TIMEOUT_MS) {
        log.warn("CONNECTION LOST, CLOSING ROOM");
        room.closeRoom();
      }
    }
  }

}
