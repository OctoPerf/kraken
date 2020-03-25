package com.kraken.grafana.client.properties;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class GrafanaClientPropertiesTest {

  public static final GrafanaClientProperties GRAFANA_CLIENT_PROPERTIES = GrafanaClientProperties
    .builder()
    .url("grafanaUrl")
    .user("grafanaUser")
    .password("grafanaPassword")
    .dashboard("grafanaDashboard")
    .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(GRAFANA_CLIENT_PROPERTIES);
  }

  @Test
  public void shouldPostConstruct() {
    GRAFANA_CLIENT_PROPERTIES.postConstruct();
  }
}
