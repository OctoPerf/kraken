package com.kraken.analysis.properties.spring;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class GrafanaPropertiesTest {

  public static final ImmutableGrafanaProperties GRAFANA_CLIENT_PROPERTIES = ImmutableGrafanaProperties
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

  @Test
  public void shouldPostConstruct() {
    GRAFANA_CLIENT_PROPERTIES.log();
  }
}
