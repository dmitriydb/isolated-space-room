package ru.shanalotte.room;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.web.servlet.MockMvc;
import ru.shanalotte.config.TopicsConfig;
import ru.shanalotte.schemas.TemperatureStateRecord;

public class TemperatureConsumerTest {

  private ObjectMapper objectMapper = new ObjectMapper();
  private MockConsumer<String, String> consumer;
  private Room room;
  private TemperatureConsumer temperatureConsumer;
  private LastTemperatureStats lastTemperatureStats;

  @BeforeEach
  public void setUp() {
    consumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);
    room = mock(Room.class);
    lastTemperatureStats = mock(LastTemperatureStats.class);
    temperatureConsumer = new TemperatureConsumer(room, lastTemperatureStats, "", consumer);
    consumer.assign(Collections.singleton(new TopicPartition(TopicsConfig.TOPIC_NAME, 0)));
    Map<TopicPartition, Long> beginningOffsets = new HashMap<>();
    beginningOffsets.put(new TopicPartition(TopicsConfig.TOPIC_NAME, 0), 0L);
    consumer.updateBeginningOffsets(beginningOffsets);
  }

  @Test
  public void should_CloseRoom_whenFreezeRecordSent() throws JsonProcessingException, InterruptedException {
    TemperatureStateRecord temperatureStateRecord = new TemperatureStateRecord("INCREASING", -100, 17);
    String recordJson = objectMapper.writeValueAsString(temperatureStateRecord);
    consumer.schedulePollTask(() -> consumer.addRecord(new ConsumerRecord<>(TopicsConfig.TOPIC_NAME, 0, 0, "1", recordJson)));

    temperatureConsumer.start();
    Thread.sleep(200L);

    verify(room).closeRoom();
    verify(lastTemperatureStats).setTemperature(-100);
    verify(lastTemperatureStats).setVector(1);
    verify(lastTemperatureStats).setSpeed(17);
  }

  @Test
  public void should_CloseRoom_whenHeatRecordSent() throws JsonProcessingException, InterruptedException {
    TemperatureStateRecord temperatureStateRecord = new TemperatureStateRecord("DECREASING", 100, 137);
    String recordJson = objectMapper.writeValueAsString(temperatureStateRecord);
    consumer.schedulePollTask(() -> consumer.addRecord(new ConsumerRecord<>(TopicsConfig.TOPIC_NAME, 0, 0, "1", recordJson)));

    temperatureConsumer.start();
    Thread.sleep(200L);

    verify(room).closeRoom();
    verify(lastTemperatureStats).setTemperature(100);
    verify(lastTemperatureStats).setVector(0);
    verify(lastTemperatureStats).setSpeed(137);
  }

  @Test
  public void should_OpenRoom_whenDoableRecordSent() throws JsonProcessingException, InterruptedException {
    TemperatureStateRecord temperatureStateRecord = new TemperatureStateRecord("DECREASING", 0, 1);
    String recordJson = objectMapper.writeValueAsString(temperatureStateRecord);
    consumer.schedulePollTask(() -> consumer.addRecord(new ConsumerRecord<>(TopicsConfig.TOPIC_NAME, 0, 0, "1", recordJson)));
    when(lastTemperatureStats.getLastTemperature()).thenReturn(new AtomicInteger(0));

    temperatureConsumer.start();
    Thread.sleep(200L);

    verify(room).openRoom();
    verify(lastTemperatureStats).setTemperature(0);
    verify(lastTemperatureStats).setVector(0);
    verify(lastTemperatureStats).setSpeed(1);
  }

}