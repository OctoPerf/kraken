package com.kraken.security.entity.user;

import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

public class KrakenFederatedIdentityTest {

  public static final KrakenFederatedIdentity KRAKEN_FEDERATED_IDENTITY = KrakenFederatedIdentity.builder()
      .identityProvider("identityProvider")
      .userId("userId")
      .userName("userName")
      .build();

  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(KRAKEN_FEDERATED_IDENTITY.getClass());
  }

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(KRAKEN_FEDERATED_IDENTITY);
  }
}