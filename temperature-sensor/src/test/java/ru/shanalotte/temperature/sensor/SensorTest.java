package ru.shanalotte.temperature.sensor;

import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

@Slf4j
public class SensorTest {

  @Test
  public void should_readPropertiesForDevProfile_whenNotRunInDocker() {
    Sensor sensor = new Sensor();
    assertThat(sensor.getServerHost()).isEqualTo("localhost");
    assertThat(sensor.getServerPort()).isEqualTo(10001);
  }

  @Test
  public void should_readPropertiesForTestProfile() {
    Sensor sensor = new Sensor("test");
    assertThat(sensor.getServerHost()).isEqualTo("localhost");
    assertThat(sensor.getServerPort()).isEqualTo(8172);
  }

}