package com.kraken.analysis.properties.spring;

import com.kraken.analysis.properties.api.GrafanaProperties;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

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
    shouldPassAll(GRAFANA_CLIENT_PROPERTIES);
  }
}
