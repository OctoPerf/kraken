package com.kraken.grafana.client;

import com.google.common.base.Charsets;
import com.kraken.grafana.client.properties.GrafanaClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Slf4j
@Configuration
class GrafanaClientConfiguration {

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
