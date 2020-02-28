package com.kraken.runtime.entity;

import org.junit.Test;

import java.util.Optional;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class ResourcesAllocationTest {

  public static final ResourcesAllocation RESOURCES_ALLOCATION = ResourcesAllocation.builder()
      .cpuRequest(1f)
      .cpuLimit(2f)
      .memoryRequest(512)
      .memoryLimit(1024)
      .memoryPercentage(Optional.of(0.8f))
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(RESOURCES_ALLOCATION);
  }

}
