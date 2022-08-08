package ru.shanalotte.temperature.sensor;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.shanalotte.config.TopicsConfig;

@Slf4j
public class SensorProducer {

  @Getter
  private String bootstrapServerHost;
  @Getter
  private int bootstrapServerPort;

  private KafkaProducer<String, String> producer;

  public SensorProducer() {
    loadProperties();
    prepareProducer();
  }

  public SensorProducer(String profile) {
    loadProperties(profile);
    prepareProducer();
  }

  private Map<String, Object> defaultConfig() {
    Map<String, Object> producerConfig = new HashMap<>();
    producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "SensorProducer");
    return producerConfig;
  }

  private Map<String, Object> configv2() {
    Map<String, Object> producerConfig = new HashMap<>();
    producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "SensorProducer");
    producerConfig.put(ProducerConfig.ACKS_CONFIG, "0");
    return producerConfig;
  }

  private void prepareProducer() {
    producer = new KafkaProducer<>(configv2());
  }

  private void loadProperties() {
    if (System.getenv().containsKey("PRODUCTION")) {
      loadProperties("production");
    } else {
      loadProperties("dev");
    }
  }

  @SneakyThrows
  private void loadProperties(String profile) {
    log.info("Profile active: {}. Loading properties...", profile);
    Properties properties = new Properties();
    properties.load(Sensor.class.getClassLoader().getResourceAsStream("application-" + profile + ".properties"));
    bootstrapServerHost = properties.getProperty("bootstrap.server.host");
    bootstrapServerPort = Integer.parseInt(properties.getProperty("bootstrap.server.port"));
    log.info("Done!");
  }

  public void sendRecord(String record) {
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TopicsConfig.TOPIC_NAME, null, record);
    log.info("Sending record {}", record);
    producer.send(producerRecord);
    producer.flush();
    log.info("Done");
  }
}
