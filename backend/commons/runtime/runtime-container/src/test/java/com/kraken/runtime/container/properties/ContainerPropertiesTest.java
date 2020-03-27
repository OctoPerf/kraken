package com.kraken.runtime.container.properties;

import com.kraken.runtime.entity.task.TaskType;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class ContainerPropertiesTest {

  public static final ImmutableContainerProperties RUNTIME_PROPERTIES = ImmutableContainerProperties.builder()
      .taskId("taskId")
      .taskType(TaskType.GATLING_RUN)
      .name("containerName")
      .hostId("hostId")
      .build();

  @Test
  public void shouldPassTestUtils() {
    RUNTIME_PROPERTIES.postConstruct();
    TestUtils.shouldPassAll(RUNTIME_PROPERTIES);
  }

}
