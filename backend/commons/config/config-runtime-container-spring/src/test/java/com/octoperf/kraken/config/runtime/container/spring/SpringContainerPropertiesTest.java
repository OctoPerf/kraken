package com.octoperf.kraken.config.runtime.container.spring;

import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class SpringContainerPropertiesTest {
  public static final SpringContainerProperties RUNTIME_PROPERTIES = SpringContainerProperties.builder()
      .taskId("taskId")
      .taskType(TaskType.GATLING_RUN)
      .name("containerName")
      .hostId("hostId")
      .userId("userId")
      .applicationId("applicationId")
      .projectId("projectId")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(RUNTIME_PROPERTIES);
  }
}