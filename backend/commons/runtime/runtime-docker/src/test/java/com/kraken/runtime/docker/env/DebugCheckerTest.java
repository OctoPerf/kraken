package com.kraken.runtime.docker.env;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.TaskType;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DebugChecker.class})
public class DebugCheckerTest {

  @Autowired
  DebugChecker checker;

  @Test
  public void shouldTest() {
    assertThat(checker.test(TaskType.RUN)).isFalse();
    assertThat(checker.test(TaskType.DEBUG)).isTrue();
    assertThat(checker.test(TaskType.RECORD)).isFalse();
  }


  @Test(expected = NullPointerException.class)
  public void shouldFailCheck() {
    checker.accept(ImmutableMap.of());
  }

  @Test
  public void shouldSucceed() {
    final var env = ImmutableMap.<String, String>builder()
        .put(KRAKEN_VERSION, "value")
        .put(KRAKEN_GATLING_SIMULATION, "value")
        .put(KRAKEN_DESCRIPTION, "value")
        .put(KRAKEN_TASK_ID, "value")
        .put(KRAKEN_EXPECTED_COUNT, "value")
        .put(KRAKEN_ANALYSIS_URL, "value")
        .put(KRAKEN_RUNTIME_URL, "value")
        .put(KRAKEN_STORAGE_URL, "value")
        .build();
    checker.accept(env);
  }

  @Test
  public void shouldTestUtils(){
    TestUtils.shouldPassNPE(DebugChecker.class);
  }
}
