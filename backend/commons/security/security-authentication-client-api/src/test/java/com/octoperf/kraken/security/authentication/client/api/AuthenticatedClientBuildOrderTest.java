package com.octoperf.kraken.security.authentication.client.api;

import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

class AuthenticatedClientBuildOrderTest {

  public static final AuthenticatedClientBuildOrder ORDER = AuthenticatedClientBuildOrder.builder()
      .mode(AuthenticationMode.SESSION)
      .userId("userId")
      .projectId("projectId")
      .applicationId("applicationId")
      .build();


  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(ORDER);
  }

  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(ORDER.getClass());
  }
}