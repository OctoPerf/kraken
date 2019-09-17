package com.kraken.grafana.client.properties;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class GrafanaClientPropertiesTest {

  public static final GrafanaClientProperties GRAFANA_CLIENT_PROPERTIES = GrafanaClientProperties.builder()
      .grafanaUrl("grafanaUrl")
      .grafanaUser("grafanaUser")
      .grafanaPassword("grafanaPassword")
      .grafanaDashboard("grafanaDashboard")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(GRAFANA_CLIENT_PROPERTIES);
  }


}
