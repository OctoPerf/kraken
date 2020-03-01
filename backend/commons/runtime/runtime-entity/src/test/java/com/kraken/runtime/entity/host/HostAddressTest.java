package com.kraken.runtime.entity.host;

import com.kraken.runtime.entity.host.HostAddress;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

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
