package com.kraken.runtime.tasks.configuration.entity;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class TaskConfigurationTest {

  public static final TaskConfiguration TASK_CONFIGURATION = TaskConfiguration.builder()
      .type("RUN")
      .file("file")
      .containersCount(2)
      .environment(ImmutableMap.of("key", "value"))
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(TASK_CONFIGURATION);
  }

}
