package com.kraken.grafana.client.properties;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {GrafanaClientPropertiesConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class GrafanaClientPropertiesConfigurationTest {

  @Autowired
  GrafanaClientProperties grafanaClientProperties;

  @Test
  public void shouldCreateProperties() {
    Assertions.assertThat(grafanaClientProperties.getGrafanaUrl()).isNotNull();
  }


}