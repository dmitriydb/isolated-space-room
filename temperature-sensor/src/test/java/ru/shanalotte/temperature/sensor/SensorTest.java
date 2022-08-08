package ru.shanalotte.temperature.sensor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import lombok.Cleanup;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

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


  @Test
  public void should_read_fromSocketServer() throws InterruptedException {
    Sensor sensor = new Sensor("test");
    Runnable testServerSocket = () -> {
      try {
        @Cleanup ServerSocket serverSocket = new ServerSocket(8172);
        @Cleanup Socket s = serverSocket.accept();
        @Cleanup PrintWriter out = new PrintWriter(s.getOutputStream());
        out.write("Hello\n");
        out.flush();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    };
    new Thread(testServerSocket).start();
    sensor.startSensing();
    Thread.sleep(150);
    assertThat(sensor.getRecordedEvents()).containsAll(Collections.singleton("Hello"));
  }
}