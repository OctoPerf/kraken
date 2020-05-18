package com.kraken.runtime.entity.host;

import org.junit.Test;

import static com.kraken.tests.utils.TestUtils.shouldPassAll;

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
