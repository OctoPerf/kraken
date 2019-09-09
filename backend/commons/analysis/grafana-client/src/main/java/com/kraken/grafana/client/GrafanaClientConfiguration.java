package com.kraken.grafana.client;

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
class GrafanaClientConfiguration {

  @Autowired
  @Bean
  GrafanaClientProperties grafanaClientProperties(@Value("${kraken.grafana.url}") final String grafanaUrl,
                                             @Value("${kraken.grafana.dashboard}") final String grafanaDashboard,
                                             @Value("${kraken.grafana.user}") final String grafanaUser,
                                             @Value("${kraken.grafana.password}") final String grafanaPassword) {
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

  @Bean("webClientGrafana")
  @Autowired
  WebClient grafanaWebClient(final GrafanaClientProperties properties) {
    final var credentials = properties.getGrafanaUser() + ":" + properties.getGrafanaPassword();
    final var encoded = Base64.getEncoder().encodeToString(credentials.getBytes(Charsets.UTF_8));
    return WebClient
        .builder()
        .baseUrl(properties.getGrafanaUrl())
        .defaultHeader("Authorization", "Basic " + encoded)
        .build();
  }

}
