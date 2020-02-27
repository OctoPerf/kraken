package com.kraken.runtie.server.properties;

import com.google.common.collect.ImmutableMap;
import com.google.common.testing.NullPointerTester;
import com.kraken.runtime.entity.ResourcesAllocation;
import com.kraken.runtime.entity.ResourcesAllocationTest;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.server.properties.RuntimeServerProperties;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

public class RuntimeServerPropertiesTest {

  public static final RuntimeServerProperties RUNTIME_SERVER_PROPERTIES = RuntimeServerProperties.builder()
      .containersCount(ImmutableMap.of(TaskType.RUN, 2, TaskType.DEBUG, 2, TaskType.RECORD, 2))
      .defaultAllocations(ResourcesAllocationTest.RESOURCES_ALLOCATION)
      .version("1.3.0")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassEquals(RUNTIME_SERVER_PROPERTIES.getClass());
    new NullPointerTester().setDefault(ResourcesAllocation.class, ResourcesAllocationTest.RESOURCES_ALLOCATION).testConstructors(RUNTIME_SERVER_PROPERTIES.getClass(), PACKAGE);
    TestUtils.shouldPassToString(RUNTIME_SERVER_PROPERTIES);
  }
}
