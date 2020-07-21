package com.octoperf.kraken.config.grafana.spring;

import com.octoperf.kraken.config.grafana.api.GrafanaProperties;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class GrafanaPropertiesTest {

  public static final GrafanaProperties GRAFANA_CLIENT_PROPERTIES = SpringGrafanaProperties
    .builder()
    .url("grafanaUrl")
    .user("grafanaUser")
    .password("grafanaPassword")
    .dashboard("grafanaDashboard")
    .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassEquals(GRAFANA_CLIENT_PROPERTIES.getClass());
    TestUtils.shouldPassToString(GRAFANA_CLIENT_PROPERTIES);
  }
}
