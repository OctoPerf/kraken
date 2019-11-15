package com.kraken.runtie.server.properties;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.server.properties.RuntimeServerProperties;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class RuntimeServerPropertiesTest {

  public static final RuntimeServerProperties RUNTIME_SERVER_PROPERTIES = RuntimeServerProperties.builder()
      .containersCount(ImmutableMap.of(TaskType.RUN, 2, TaskType.DEBUG, 2, TaskType.RECORD, 2))
      .version("1.3.0")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(RUNTIME_SERVER_PROPERTIES);
  }
}
