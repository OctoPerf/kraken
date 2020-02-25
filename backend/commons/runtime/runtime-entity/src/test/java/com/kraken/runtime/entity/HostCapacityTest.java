package com.kraken.runtime.entity;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class HostCapacityTest {

  public static final HostCapacity HOST_CAPACITY = HostCapacity.builder()
      .cpu("2")
      .memory("64Gb")
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(HOST_CAPACITY);
  }

}
