package ru.shanalotte.temperature.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import ru.shanalotte.config.TemperatureSocketServerConfig;
import ru.shanalotte.temperature.generator.SimpleTemperatureGenerator;

class TemperatureSocketServerTest {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void shouldRunOnDefaultPortIfTSocketVariableMissing() {
    if (System.getenv(TemperatureSocketServerConfig.PORT_ENV_VARIABLE) != null) {
      return;
    }
    TemperatureSocketServer socketServer = new TemperatureSocketServer(new SimpleTemperatureGenerator());
    assertThat(socketServer.getPort())
        .isEqualTo(TemperatureSocketServerConfig.DEFAULT_PORT);
  }

  @Test
  public void shouldRunOnTSocketServerVariableIfSet() throws InterruptedException {
    if (System.getenv(TemperatureSocketServerConfig.PORT_ENV_VARIABLE) == null) {
      return;
    }
    TemperatureSocketServer socketServer = new TemperatureSocketServer(new SimpleTemperatureGenerator());
    assertThat(String.valueOf(socketServer.getPort()))
        .isEqualTo(System.getenv(TemperatureSocketServerConfig.PORT_ENV_VARIABLE));
  }

}