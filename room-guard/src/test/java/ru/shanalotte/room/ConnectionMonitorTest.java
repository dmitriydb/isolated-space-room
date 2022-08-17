package ru.shanalotte.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import ru.shanalotte.config.TemperatureGeneratorConfig;

public class ConnectionMonitorTest {

  @Test
  public void should_closeRoom_ifTimeoutExceeded() throws InterruptedException {
    Room room = spy(new Room());
    ConnectionMonitor connectionMonitor = new ConnectionMonitor(room);
    room.attachConnectionMonitor(connectionMonitor);
    room.openRoom();

    connectionMonitor.start();
    Thread.sleep(TemperatureGeneratorConfig.TEMPERATURE_CHANGE_TIMEOUT_MS * 2);

    verify(room, atLeast(1)).closeRoom();
    assertThat(room.state()).isEqualTo("closed");
  }

}