package com.octoperf.kraken.runtime.tasks.configuration.entity;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class TaskConfigurationTest {

  public static final TaskConfiguration TASK_CONFIGURATION = TaskConfiguration.builder()
      .type(TaskType.GATLING_RUN)
      .file("file")
      .containersCount(2)
      .environment(ImmutableMap.of("key", "value"))
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(TASK_CONFIGURATION);
  }

}
