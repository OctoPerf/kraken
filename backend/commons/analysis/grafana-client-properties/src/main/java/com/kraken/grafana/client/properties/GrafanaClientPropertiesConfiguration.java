package com.kraken.grafana.client.properties;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Slf4j
@Configuration
class GrafanaClientPropertiesConfiguration {

  @Autowired
  @Bean
  GrafanaClientProperties grafanaClientProperties(@Value("${kraken.grafana.url:#{environment.KRAKEN_GRAFANA_URL}}") final String grafanaUrl,
                                             @Value("${kraken.grafana.dashboard:#{environment.KRAKEN_GRAFANA_DASHBOARD}}") final String grafanaDashboard,
                                             @Value("${kraken.grafana.user:#{environment.KRAKEN_GRAFANA_USER}}") final String grafanaUser,
                                             @Value("${kraken.grafana.password:#{environment.KRAKEN_GRAFANA_PASSWORD}}") final String grafanaPassword) {
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
