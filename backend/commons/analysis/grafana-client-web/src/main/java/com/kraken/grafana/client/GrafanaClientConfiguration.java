package com.kraken.grafana.client;

import com.kraken.config.grafana.api.GrafanaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Configuration
class GrafanaClientConfiguration {

  @Bean("webClientGrafana")
  @Autowired
  WebClient grafanaWebClient(final GrafanaProperties grafana) {
    final var credentials = grafana.getUser() + ":" + grafana.getPassword();
    final var encoded = Base64.getEncoder().encodeToString(credentials.getBytes(UTF_8));
    return WebClient
        .builder()
        .baseUrl(grafana.getUrl())
        .defaultHeader("Authorization", "Basic " + encoded)
        .build();
  }

}
