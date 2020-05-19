package com.kraken.grafana.client.web;

import com.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class UpdateGrafanaUserRequestTest {

  public static final UpdateGrafanaUserRequest REQUEST = UpdateGrafanaUserRequest.builder()
      .name("name")
      .email("email")
      .login("login")
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(REQUEST.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(UpdateGrafanaUserRequest.class);
  }

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(REQUEST);
  }

}