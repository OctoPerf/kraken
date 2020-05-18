package com.kraken.analysis.entity;

import org.junit.Test;

import static com.kraken.tests.utils.TestUtils.shouldPassAll;

public class GrafanaLoginTest {

  public static final GrafanaLogin GRAFANA_LOGIN = GrafanaLogin.builder()
      .session("session")
      .url("url")
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(GRAFANA_LOGIN);
  }

}
