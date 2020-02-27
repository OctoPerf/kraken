package com.kraken.runtime.entity;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class ResourcesAllocationTest {

  public static final ResourcesAllocation RESOURCES_ALLOCATION = ResourcesAllocation.builder()
      .cpuRequest("1")
      .cpuLimit("2")
      .memoryRequest("512M")
      .memoryLimit("1024M")
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(RESOURCES_ALLOCATION);
  }

}
