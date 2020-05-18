package com.kraken.config.runtime.container.spring;

import com.kraken.runtime.entity.task.TaskType;
import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

public class SpringContainerPropertiesTest {
  public static final SpringContainerProperties RUNTIME_PROPERTIES = SpringContainerProperties.builder()
      .taskId("taskId")
      .taskType(TaskType.GATLING_RUN)
      .name("containerName")
      .hostId("hostId")
      .userId("userId")
      .applicationId("applicationId")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(RUNTIME_PROPERTIES);
  }
}