package ru.shanalotte.temperature.sensor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    Thread.sleep(150);
    verify(sensorProducer, times(3)).sendRecord(recordSendCaptor.capture());
    List<String> actuallySendRecords = recordSendCaptor.getAllValues();
    assertThat(actuallySendRecords).contains("a", "c", "b");
  }

  @Test
  public void should_notSendDuplicateEvents() throws InterruptedException {
    new Thread(new FakeServer("a", "a", "b")).start();
    Sensor sensor = new Sensor("test");
    SensorProducer sensorProducer = Mockito.mock(SensorProducer.class);
    sensor.attachProducer(sensorProducer);
    sensor.startSensing();
    Thread.sleep(150);
    verify(sensorProducer, times(2)).sendRecord(any());
  }

  @Test
  public void should_sendDuplicateEventsThatAreApartFromEachOther() throws InterruptedException {
    new Thread(new FakeServer("a", "b", "a", "b")).start();
    Sensor sensor = new Sensor("test");
    SensorProducer sensorProducer = Mockito.mock(SensorProducer.class);
    sensor.attachProducer(sensorProducer);
    sensor.startSensing();
    Thread.sleep(100);
    verify(sensorProducer, times(4)).sendRecord(any());
  }

  @Test
  public void should_readAgainAfterServerIsShuttedDown() throws InterruptedException {
    new Thread(new FakeServer("a", "b", "c")).start();
    CountDownLatch countDownLatch = new CountDownLatch(1);
    new Thread(new FakeServer(countDownLatch, "d", "e", "f")).start();
    Sensor sensor = new Sensor("test");
    SensorProducer sensorProducer = Mockito.mock(SensorProducer.class);
    sensor.attachProducer(sensorProducer);
    sensor.startSensing();
    Thread.sleep(200);
    countDownLatch.countDown();
    Thread.sleep(200);
    assertThat(sensor.getRecordedEvents()).contains("a", "c", "b", "d", "e", "f");
  }

  private static class FakeServer implements Runnable{
    private final String[] recordsToSend;
    private CountDownLatch countDownLatch;

    public FakeServer(String... recordsToSend) {
      this.recordsToSend = recordsToSend;
    }

    public FakeServer(CountDownLatch countDownLatch, String... recordsToSend) {
      this.recordsToSend = recordsToSend;
      this.countDownLatch = countDownLatch;
    }


    @Override
    public void run() {
      try {
        if (countDownLatch != null) {
          countDownLatch.await();
        }
        log.info("Starting server...");
        @Cleanup ServerSocket serverSocket = new ServerSocket(8172);
        log.info("Started server");
        @Cleanup Socket s = serverSocket.accept();
        log.info("Got connection");
        @Cleanup PrintWriter out = new PrintWriter(s.getOutputStream());
        for (String record : recordsToSend) {
          out.write(record + "\n");
        }
        out.flush();
        System.out.println("Server is shutting down.");
      } catch (Exception ex) {
        log.error("", ex);
      }
    }
  }

}