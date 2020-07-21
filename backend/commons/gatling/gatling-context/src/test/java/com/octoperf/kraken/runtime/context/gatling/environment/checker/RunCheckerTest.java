package com.octoperf.kraken.runtime.context.gatling.environment.checker;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RunChecker.class})
public class RunCheckerTest {

  @Autowired
  RunChecker checker;

  @Test
  public void shouldTest() {
    assertThat(checker.test(TaskType.GATLING_RUN)).isTrue();
    assertThat(checker.test(TaskType.GATLING_DEBUG)).isFalse();
    assertThat(checker.test(TaskType.GATLING_RECORD)).isFalse();
  }


  @Test
  public void shouldFailCheck() {
    Assertions.assertThrows(NullPointerException.class, () -> {
      checker.accept(ImmutableMap.of());
    });
  }

  @Test
  public void shouldSucceed() {
    final var env = ImmutableMap.<String, String>builder()
        .put(KRAKEN_GATLING_SIMULATION_NAME.name(), "value")
        .put(KRAKEN_INFLUXDB_URL.name(), "value")
        .put(KRAKEN_INFLUXDB_DATABASE.name(), "value")
        .put(KRAKEN_INFLUXDB_USER.name(), "value")
        .put(KRAKEN_INFLUXDB_PASSWORD.name(), "value")
        .build();
    checker.accept(env);
  }

  @Test
  public void shouldTestUtils() {
    TestUtils.shouldPassNPE(RunChecker.class);
  }
}
