package ru.shanalotte.room;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import ru.shanalotte.config.TopicsConfig;
import ru.shanalotte.constants.TemperatureConstants;
import ru.shanalotte.schemas.TemperatureStateRecord;

@Slf4j
public class TemperatureConsumer extends Thread{

  private Room room;
  private LastTemperatureStats lastTemperatureStats;
  private String bootstrapURL;
  private Consumer<String, String> consumer;

  public TemperatureConsumer(Room room, LastTemperatureStats lastTemperatureStats, String bootstrapURL) {
    this.room = room;
    this.lastTemperatureStats = lastTemperatureStats;
    this.bootstrapURL = bootstrapURL;
    consumer = new KafkaConsumer<>(consumerConfig());
    consumer.subscribe(Collections.singletonList(TopicsConfig.TOPIC_NAME));
  }

  public TemperatureConsumer(Room room, LastTemperatureStats lastTemperatureStats, String bootstrapURL, Consumer<String, String> consumer) {
    this.room = room;
    this.lastTemperatureStats = lastTemperatureStats;
    this.bootstrapURL = bootstrapURL;
    this.consumer = consumer;
  }

  public void run() {
    ObjectMapper objectMapper = new ObjectMapper();
    log.info("Start listening!");
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100L));
      for (ConsumerRecord<String, String> record : records) {
        try {
          TemperatureStateRecord temperatureStateRecord = objectMapper.readValue(record.value(), TemperatureStateRecord.class);
          log.info("[{}], Read temperature {}", LocalDateTime.now(), temperatureStateRecord.getCurrentTemperature());
          decideAboutRoomState(temperatureStateRecord);
          saveStats(temperatureStateRecord);
        } catch (JsonProcessingException e) {
          log.info("Skipping record {}", record);
        }
      }
    }
  }

  private Map<String, Object> consumerConfig() {
    Map<String, Object> consumerConfigMap = new HashMap<>();
    consumerConfigMap.put(ConsumerConfig.GROUP_ID_CONFIG, "room");
    consumerConfigMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapURL);
    consumerConfigMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    consumerConfigMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    consumerConfigMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    consumerConfigMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    return consumerConfigMap;
  }

  private void decideAboutRoomState(TemperatureStateRecord temperatureStateRecord) {
    int current = temperatureStateRecord.getCurrentTemperature();
    if (isNotDoableTemperature(current)) {
      room.closeRoom();
    }
    if (lastTemperatureStats.getLastTemperature() == null) {
      return;
    }
    int nextTemperature = current + (current - lastTemperatureStats.getLastTemperature().get());
    if (isNotDoableTemperature(nextTemperature)) {
      room.closeRoom();
    } else {
      room.openRoom();
    }
  }

  private boolean isNotDoableTemperature(int nextTemperature) {
    return nextTemperature >= TemperatureConstants.MAX_DOABLE_TEMPERATURE || nextTemperature <= TemperatureConstants.MIN_DOABLE_TEMPERATURE;
  }

  private void saveStats(TemperatureStateRecord temperatureStateRecord) {
    lastTemperatureStats.setTemperature(temperatureStateRecord.getCurrentTemperature());
    lastTemperatureStats.setVector(temperatureStateRecord.getVector().equals("INCREASING") ? 1 : 0);
    lastTemperatureStats.setSpeed(temperatureStateRecord.getChangeSpeed());
  }
}
