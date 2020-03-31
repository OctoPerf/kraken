package com.kraken.security.configuration.entity;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static com.kraken.security.configuration.entity.KrakenRole.USER;
import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class KrakenUserTest {

  public static final KrakenUser KRAKEN_USER = KrakenUser.builder()
      .userId("user-id")
      .username("username")
      .roles(ImmutableList.of(USER))
      .groups(ImmutableList.of("/default-kraken"))
      .currentGroup("/default-kraken")
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(KRAKEN_USER);
  }

}
