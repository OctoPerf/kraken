package com.kraken.grafana.client.web;

import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

public class FindGrafanaUserResponseTest {

  public static final FindGrafanaUserResponse RESPONSE = FindGrafanaUserResponse.builder()
      .id(2L)
      .orgId(42L)
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(RESPONSE.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(FindGrafanaUserResponse.class);
  }

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(RESPONSE);
  }

}