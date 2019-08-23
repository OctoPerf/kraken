package com.kraken.grafana.client;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {GrafanaClientConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class GrafanaClientPropertiesConfigurationTest {

  @Autowired
  GrafanaClientProperties grafanaClientProperties;

  @Qualifier("webClientGrafana")
  @Autowired
  WebClient grafanaWebClient;

  @Test
  public void shouldCreateProperties() {
    Assertions.assertThat(grafanaClientProperties.getGrafanaUrl()).isNotNull();
  }

  @Test
  public void shouldCreateWebClients() {
    Assertions.assertThat(grafanaWebClient).isNotNull();
  }

}