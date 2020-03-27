package com.kraken.runtime.container.properties;

import com.kraken.runtime.entity.task.TaskType;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class ContainerPropertiesTest {

  public static final SpringContainerProperties RUNTIME_PROPERTIES = SpringContainerProperties.builder()
      .taskId("taskId")
      .taskType(TaskType.GATLING_RUN)
      .name("containerName")
      .hostId("hostId")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(RUNTIME_PROPERTIES);
  }

}
