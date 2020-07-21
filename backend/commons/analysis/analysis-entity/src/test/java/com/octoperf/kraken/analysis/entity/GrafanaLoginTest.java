package com.octoperf.kraken.analysis.entity;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class GrafanaLoginTest {

  public static final GrafanaLogin GRAFANA_LOGIN = GrafanaLogin.builder()
      .session("session")
      .url("url")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(GRAFANA_LOGIN);
  }

}
