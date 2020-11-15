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

import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_GATLING_JAVA_OPTS;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JavaOptsChecker.class})
public class JavaOptsCheckerTest {

  @Autowired
  JavaOptsChecker checker;

  @Test
  public void shouldTest() {
    assertThat(checker.test(TaskType.GATLING_RUN)).isTrue();
    assertThat(checker.test(TaskType.GATLING_DEBUG)).isTrue();
    assertThat(checker.test(TaskType.GATLING_RECORD)).isTrue();
  }


  @Test
  public void shouldFailCheck() {
    Assertions.assertThrows(NullPointerException.class, () -> checker.accept(ImmutableMap.of()));
  }

  @Test
  public void shouldSucceed() {
    final var env = ImmutableMap.<String, String>builder()
        .put(KRAKEN_GATLING_JAVA_OPTS.name(), "value")
        .build();
    Assertions.assertDoesNotThrow(() -> checker.accept(env));
  }

  @Test
  public void shouldTestUtils() {
    TestUtils.shouldPassNPE(JavaOptsChecker.class);
  }
}
