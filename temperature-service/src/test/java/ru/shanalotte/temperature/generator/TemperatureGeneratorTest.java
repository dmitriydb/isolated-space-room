package ru.shanalotte.temperature.generator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import ru.shanalotte.config.TemperatureGeneratorConfig;
import ru.shanalotte.constants.TemperatureConstants;
import ru.shanalotte.schemas.TemperatureState;
import ru.shanalotte.schemas.TemperatureVector;

public class TemperatureGeneratorTest {

  @Test
  public void should_attainDoableTemperature_whenStarted() {
    TemperatureGenerator temperatureGenerator = new SimpleTemperatureGenerator();
    temperatureGenerator.start();
    assertThat(temperatureGenerator.getCurrentTemperature()).isLessThan(TemperatureConstants.MAX_DOABLE_TEMPERATURE);
  }

  @Test
  public void should_changeCurrentTemperatureVectorWithAtLeast10Seconds() throws InterruptedException {
    TemperatureGenerator temperatureGenerator = new SimpleTemperatureGenerator();
    int now = temperatureGenerator.getCurrentTemperature();
    temperatureGenerator.start();
    Set<TemperatureVector> vectors = new HashSet<>();
    for (int i = 0; i < 10; i++) {
      vectors.add(temperatureGenerator.getCurrentTemperatureVector());
      Thread.sleep(TemperatureGeneratorConfig.TEMPERATURE_CHANGE_TIMEOUT_MS);
    }
    int after = temperatureGenerator.getCurrentTemperature();
    assertThat(now).isNotEqualTo(after);
  }

  @Test
  public void should_ChangeTemperatureSpeed() throws InterruptedException {
    TemperatureStateListener listener = new CollectionTemperatureStateListener();
    TemperatureGenerator temperatureGenerator = new SimpleTemperatureGenerator();
    temperatureGenerator.addListener(listener);
    temperatureGenerator.start();
    Thread.sleep(TemperatureGeneratorConfig.TEMPERATURE_CHANGE_TIMEOUT_MS * 5);
    List<Integer> recordedTemperatureValues = listener.recordedEvents().stream().map(TemperatureState::getTemperature)
        .collect(Collectors.toList());
    assertThat(recordedTemperatureValues).hasSizeGreaterThan(4);
  }

}
