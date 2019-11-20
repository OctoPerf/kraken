package com.kraken.grafana.client.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.kraken.tools.environment.KrakenEnvironmentAtValues.*;

@Slf4j
@Configuration
class GrafanaClientPropertiesConfiguration {

  @Autowired
  @Bean
  GrafanaClientProperties grafanaClientProperties(@Value($KRAKEN_GRAFANA_URL) final String grafanaUrl,
                                                  @Value($KRAKEN_GRAFANA_DASHBOARD) final String grafanaDashboard,
                                                  @Value($KRAKEN_GRAFANA_USER) final String grafanaUser,
                                                  @Value($KRAKEN_GRAFANA_PASSWORD) final String grafanaPassword) {
    log.info("Grafana URL is set to " + grafanaUrl);
    log.info("Grafana dashboard path is set to " + grafanaDashboard);
    log.info("Grafana user path is set to " + grafanaUser);

    return GrafanaClientProperties.builder()
        .grafanaUrl(grafanaUrl)
        .grafanaDashboard(grafanaDashboard)
        .grafanaUser(grafanaUser)
        .grafanaPassword(grafanaPassword)
        .build();
  }

}
