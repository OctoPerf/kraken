package com.kraken.runtime.container.properties;

import com.kraken.runtime.entity.TaskType;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class RuntimeContainerPropertiesTest {

  public static final RuntimeContainerProperties RUNTIME_PROPERTIES = RuntimeContainerProperties.builder()
      .taskId("taskId")
      .taskType(TaskType.RUN)
      .containerId("containerId")
      .groupId("groupId")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(RUNTIME_PROPERTIES);
  }

}
