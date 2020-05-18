package com.kraken.runtime.entity.host;

import org.junit.Test;

import static com.kraken.tests.utils.TestUtils.shouldPassAll;

public class HostAddressTest {

  public static final HostAddress HOST_ADDRESS = HostAddress.builder()
      .address("address")
      .type("type")
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(HOST_ADDRESS);
  }

}
