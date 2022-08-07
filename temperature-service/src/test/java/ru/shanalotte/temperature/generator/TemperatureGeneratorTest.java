package ru.shanalotte.temperature.generator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import ru.shanalotte.temperature.TemperatureConstants;

public class TemperatureGeneratorTest {

  @Test
  public void should_attainDoableTemperature_whenStarted() {
    TemperatureGenerator temperatureGenerator = new SimpleTemperatureGenerator();
    temperatureGenerator.start();
    assertThat(temperatureGenerator.getCurrentTemperature()).isLessThan(TemperatureConstants.MAX_DOUBLE_TEMPERATURE);
  }

  @Test
  public void should_changeCurrentTemperatureVectorWithAtLeast10Seconds() throws InterruptedException {
    TemperatureGenerator temperatureGenerator = new SimpleTemperatureGenerator();
    temperatureGenerator.start();
    Set<TemperatureVector> vectors = new HashSet<>();
    for (int i = 0; i < 10; i++) {
      vectors.add(temperatureGenerator.getCurrentTemperatureVector());
      Thread.sleep(1000);
    }
    assertThat(vectors).hasSizeGreaterThan(1);
  }

  @Test
  public void should_ChangeTemperatureSpeed() throws InterruptedException {
    TemperatureStateListener listener = new CollectionTemperatureStateListener();
    TemperatureGenerator temperatureGenerator = new SimpleTemperatureGenerator();
    temperatureGenerator.addListener(listener);
    temperatureGenerator.start();
    Thread.sleep(5000);
    List<Integer> recordedTemperatureValues = listener.recordedEvents().stream().map(TemperatureState::getTemperature)
        .collect(Collectors.toList());
    assertThat(recordedTemperatureValues).hasSizeGreaterThan(4);
  }

}
