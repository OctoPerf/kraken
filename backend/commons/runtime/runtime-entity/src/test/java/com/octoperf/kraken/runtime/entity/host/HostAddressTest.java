package com.octoperf.kraken.runtime.entity.host;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class HostAddressTest {

  public static final HostAddress HOST_ADDRESS = HostAddress.builder()
      .address("address")
      .type("type")
      .build();


  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(HOST_ADDRESS);
  }

}
