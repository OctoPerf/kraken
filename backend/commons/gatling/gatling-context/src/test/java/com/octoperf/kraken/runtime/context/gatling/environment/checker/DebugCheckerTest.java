package com.octoperf.kraken.runtime.context.gatling.environment.checker;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_GATLING_SIMULATION_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DebugChecker.class})
public class DebugCheckerTest {

  @Autowired
  DebugChecker checker;

  @Test
  public void shouldTest() {
    assertThat(checker.test(TaskType.GATLING_RUN)).isFalse();
    assertThat(checker.test(TaskType.GATLING_DEBUG)).isTrue();
    assertThat(checker.test(TaskType.GATLING_RECORD)).isFalse();
  }


  @Test
  public void shouldFailCheck() {
    Assertions.assertThrows(NullPointerException.class, () -> checker.accept(ImmutableMap.of()));
  }

  @Test
  public void shouldSucceed() {
    final var env = ImmutableMap.<String, String>builder()
        .put(KRAKEN_GATLING_SIMULATION_NAME.name(), "value")
        .build();
    Assertions.assertDoesNotThrow(() -> checker.accept(env));
  }

  @Test
  public void shouldTestUtils() {
    TestUtils.shouldPassNPE(DebugChecker.class);
  }
}
