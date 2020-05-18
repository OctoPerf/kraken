package com.kraken.security.entity.user;

import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

public class KrakenCredentialTest {

  public static final KrakenCredential KRAKEN_CREDENTIAL = KrakenCredential.builder()
      .createdDate(0L)
      .credentialData("credentialData")
      .id("id")
      .priority(0)
      .secretData("secretData")
      .temporary(false)
      .type("type")
      .userLabel("userLabel")
      .value("value")
      .build();

  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(KRAKEN_CREDENTIAL.getClass());
  }

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(KRAKEN_CREDENTIAL);
  }
}