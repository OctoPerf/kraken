package com.kraken.runtime.container.properties;

import com.kraken.runtime.entity.task.TaskType;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class RuntimeContainerPropertiesTest {

  public static final ImmutableRuntimeContainerProperties RUNTIME_PROPERTIES = ImmutableRuntimeContainerProperties.builder()
      .taskId("taskId")
      .taskType(TaskType.GATLING_RUN)
      .containerName("containerName")
      .hostId("hostId")
      .build();

  @Test
  public void shouldPassTestUtils() {
    RUNTIME_PROPERTIES.postConstruct();
    TestUtils.shouldPassAll(RUNTIME_PROPERTIES);
  }

}
