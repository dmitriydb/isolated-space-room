package ru.shanalotte.room.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.shanalotte.room.LastTemperatureStats;
import ru.shanalotte.room.Room;
import ru.shanalotte.room.rest.dto.RoomStatsDto;

public class MainRestControllerTest {

  @Test
  public void statsEndpointShouldReturnDefaultStateIfConsumersAreNotRunningYet() throws Exception {
    RoomStatsDto roomStatsDto = new MainRestController().getRoomStats();
    assertThat(isDefault(roomStatsDto)).isTrue();
  }

  @Test
  public void controllerShouldUseLastRoomStats() {
    Room room = mock(Room.class);
    LastTemperatureStats lastTemperatureStats = spy(new LastTemperatureStats());
    when(room.state()).thenReturn("open");
    MainRestController mainRestController = new MainRestController();
    mainRestController.setLastTemperatureStats(lastTemperatureStats);
    mainRestController.setRoom(room);

    mainRestController.getRoomStats();

    verify(lastTemperatureStats).getTemperature();
    verify(lastTemperatureStats).getVector();
    verify(lastTemperatureStats).getChangeSpeed();
    verify(room).state();
  }

  @Test
  public void controllerShouldReturnCorrectLastRoomStats() {
    Room room = mock(Room.class);
    LastTemperatureStats lastTemperatureStats = spy(new LastTemperatureStats(1, 2, 3));
    when(room.state()).thenReturn("open");
    MainRestController mainRestController = new MainRestController();
    mainRestController.setLastTemperatureStats(lastTemperatureStats);
    mainRestController.setRoom(room);

    RoomStatsDto response = mainRestController.getRoomStats();

    assertThat(response.getSpeed()).isEqualTo(2);
    assertThat(response.getRoomState()).isEqualTo("open");
    assertThat(response.getVector()).isEqualTo(1);
    assertThat(response.getTemperature()).isEqualTo(3);
  }

  private boolean isDefault(RoomStatsDto roomStatsDto) {
    return roomStatsDto.getTemperature() == 0 && roomStatsDto.getRoomState().equals("closed") && roomStatsDto.getSpeed() == 0 && roomStatsDto.getVector() == 0;
  }

}