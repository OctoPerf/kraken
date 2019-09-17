package com.kraken.grafana.client.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrafanaClientPropertiesTestConfiguration {

  @Bean
  GrafanaClientProperties properties() {
    return GrafanaClientPropertiesTest.GRAFANA_CLIENT_PROPERTIES;
  }

}
