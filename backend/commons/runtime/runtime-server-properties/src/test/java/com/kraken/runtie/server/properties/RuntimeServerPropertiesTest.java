package com.kraken.runtie.server.properties;

import com.google.common.collect.ImmutableMap;
import com.google.common.testing.NullPointerTester;
import com.kraken.runtime.entity.ResourcesAllocation;
import com.kraken.runtime.entity.ResourcesAllocationTest;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.server.properties.RuntimeServerProperties;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

import java.util.Optional;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static java.util.Optional.of;

public class RuntimeServerPropertiesTest {

  public static final RuntimeServerProperties RUNTIME_SERVER_PROPERTIES = RuntimeServerProperties.builder()
      .containersCount(ImmutableMap.of(TaskType.RUN, 2, TaskType.DEBUG, 2, TaskType.RECORD, 2))
      .defaultAllocations(
          ImmutableMap.of(
              TaskType.RUN, ImmutableMap.of("gatling", ResourcesAllocation.builder().cpuRequest(1f).cpuLimit(2f).memoryRequest(512).memoryLimit(1024).memoryPercentage(of(0.9f)).build(),
                  "telegraf", ResourcesAllocation.builder().cpuRequest(1f).cpuLimit(2f).memoryRequest(512).memoryLimit(1024).memoryPercentage(of(0.9f)).build()),
              TaskType.DEBUG, ImmutableMap.of("gatling", ResourcesAllocation.builder().cpuRequest(1f).cpuLimit(2f).memoryRequest(512).memoryLimit(1024).memoryPercentage(of(0.9f)).build(),
                  "log-parser", ResourcesAllocation.builder().cpuRequest(1f).cpuLimit(2f).memoryRequest(512).memoryLimit(1024).memoryPercentage(of(0.9f)).build()),
              TaskType.RECORD, ImmutableMap.of("gatling", ResourcesAllocation.builder().cpuRequest(1f).cpuLimit(2f).memoryRequest(512).memoryLimit(1024).memoryPercentage(of(0.9f)).build(),
                  "har-parser", ResourcesAllocation.builder().cpuRequest(1f).cpuLimit(2f).memoryRequest(512).memoryLimit(1024).memoryPercentage(of(0.9f)).build())
          )
      )
      .version("1.3.0")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassEquals(RUNTIME_SERVER_PROPERTIES.getClass());
    new NullPointerTester().setDefault(ResourcesAllocation.class, ResourcesAllocationTest.RESOURCES_ALLOCATION).testConstructors(RUNTIME_SERVER_PROPERTIES.getClass(), PACKAGE);
    TestUtils.shouldPassToString(RUNTIME_SERVER_PROPERTIES);
  }
}
