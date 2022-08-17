package ru.shanalotte.room;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class RoomTest {

  @Test
  public void should_DoRightStuff_whenOpeningRoom() {
    Room room = new Room();
    ConnectionMonitor connectionMonitor = spy(new ConnectionMonitor(room));
    room.attachConnectionMonitor(connectionMonitor);

    room.openRoom();

    assertThat(room.state()).isEqualTo("open");
    verify(connectionMonitor).updateTime();
  }

  @Test
  public void should_DoRightStuff_whenClosingRoom() {
    Room room = new Room();
    ConnectionMonitor connectionMonitor = spy(new ConnectionMonitor(room));
    room.attachConnectionMonitor(connectionMonitor);

    room.closeRoom();

    assertThat(room.state()).isEqualTo("closed");
    verify(connectionMonitor).updateTime();
  }

}