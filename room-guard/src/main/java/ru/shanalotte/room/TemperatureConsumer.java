package ru.shanalotte.room;

import java.time.Duration;
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

@RequiredArgsConstructor
@Slf4j
public class TemperatureConsumer extends Thread{

  private final Room room;

  public void run() {
    Map<String, Object> consumerConfigMap = new HashMap<>();
    ObjectMapper objectMapper = new ObjectMapper();
    consumerConfigMap.put(ConsumerConfig.GROUP_ID_CONFIG, "room");
    consumerConfigMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    consumerConfigMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    consumerConfigMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    consumerConfigMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfigMap);
    consumer.subscribe(Collections.singletonList(TopicsConfig.TOPIC_NAME));
    log.info("Start listening!");
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100L));
      for (ConsumerRecord<String, String> record : records) {
        try {
          TemperatureStateRecord temperatureStateRecord = objectMapper.readValue(record.value(), TemperatureStateRecord.class);
          log.info("Read temperature {}", temperatureStateRecord.getCurrentTemperature());
          if (temperatureStateRecord.getCurrentTemperature() >= TemperatureConstants.MAX_DOUBLE_TEMPERATURE) {
            room.closeRoom();
          } else if (room.isClosed()){
            room.openRoom();
          }
        } catch (JsonProcessingException e) {
          log.info("Skipping record {}", record);
        }
      }
    }
  }
}
