package com.kraken.runtime.tasks.configuration.entity;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.task.TaskType;
import org.junit.Test;

import static com.kraken.tests.utils.TestUtils.shouldPassAll;

public class TaskConfigurationTest {

  public static final TaskConfiguration TASK_CONFIGURATION = TaskConfiguration.builder()
      .type(TaskType.GATLING_RUN)
      .file("file")
      .containersCount(2)
      .environment(ImmutableMap.of("key", "value"))
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(TASK_CONFIGURATION);
  }

}
