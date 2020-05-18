package com.kraken.security.entity.user;

import com.google.common.collect.ImmutableList;
import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

public class KrakenUserConsentTest {

  public static final KrakenUserConsent KRAKEN_USER_CONSENT = KrakenUserConsent.builder()
      .clientId("clientId")
      .createdDate(0L)
      .grantedClientScopes(ImmutableList.of("grantedClientScope"))
      .lastUpdatedDate(0L)
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(KRAKEN_USER_CONSENT.getClass());
  }

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(KRAKEN_USER_CONSENT);
  }
}