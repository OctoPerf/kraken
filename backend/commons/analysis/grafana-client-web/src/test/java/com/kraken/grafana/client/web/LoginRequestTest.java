package com.kraken.grafana.client.web;

import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

public class LoginRequestTest {

  public static final LoginRequest REQUEST = LoginRequest.builder()
      .user("user")
      .email("email")
      .password("password")
      .build();

  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(REQUEST.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(LoginRequest.class);
  }

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(REQUEST);
  }
}