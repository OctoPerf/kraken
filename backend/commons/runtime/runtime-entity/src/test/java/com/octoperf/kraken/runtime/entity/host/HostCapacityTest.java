package com.octoperf.kraken.runtime.entity.host;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class HostCapacityTest {

  public static final HostCapacity HOST_CAPACITY = HostCapacity.builder()
      .cpu("2")
      .memory("64Gb")
      .build();


  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(HOST_CAPACITY);
  }

}
