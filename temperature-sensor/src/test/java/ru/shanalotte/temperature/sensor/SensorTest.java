package ru.shanalotte.temperature.sensor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Cleanup;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    new Thread(new FakeServer("Hello")).start();
    sensor.startSensing();
    Thread.sleep(150);
    assertThat(sensor.getRecordedEvents()).containsAll(Collections.singleton("Hello"));
  }

  @Test
  public void should_sendRecordedEventsToTopic() throws InterruptedException {
    new Thread(new FakeServer("a", "b", "c")).start();
    Sensor sensor = new Sensor("test");
    SensorProducer sensorProducer = Mockito.mock(SensorProducer.class);
    sensor.attachProducer(sensorProducer);
    sensor.startSensing();
    ArgumentCaptor<String> recordSendCaptor = ArgumentCaptor.forClass(String.class);
    verify(sensorProducer, times(3)).sendRecord(recordSendCaptor.capture());
    Thread.sleep(150);
    List<String> actuallySendRecords = recordSendCaptor.getAllValues();
    assertThat(actuallySendRecords).contains("a", "c", "b");
  }

  private class FakeServer implements Runnable{
    private final String[] recordsToSend;

    public FakeServer(String... recordsToSend) {
      this.recordsToSend = recordsToSend;
    }

    @Override
    public void run() {
      try {
        @Cleanup ServerSocket serverSocket = new ServerSocket(8172);
        @Cleanup Socket s = serverSocket.accept();
        @Cleanup PrintWriter out = new PrintWriter(s.getOutputStream());
        for (String record : recordsToSend) {
          out.write(record + "\n");
        }
        out.flush();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

}