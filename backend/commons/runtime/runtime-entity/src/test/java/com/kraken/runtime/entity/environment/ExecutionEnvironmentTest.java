package com.kraken.runtime.entity.environment;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.environment.ExecutionEnvironment;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static org.assertj.core.api.Assertions.assertThat;

public class ExecutionEnvironmentTest {

  public static final ExecutionEnvironment EXECUTION_ENVIRONMENT = ExecutionEnvironment.builder()
      .taskType("RUN")
      .description("description")
      .environment(ImmutableMap.of("foo", "bar"))
      .hosts(ImmutableMap.of("hostId", ImmutableMap.of("key", "value")))
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(EXECUTION_ENVIRONMENT);
  }

}
