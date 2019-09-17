package com.kraken.influxdb.client;

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
class InfluxDBClientConfiguration {

  @Bean("webClientInfluxdb")
  @Autowired
  WebClient influxdbWebClient(final InfluxDBClientProperties properties) {
    final var credentials = properties.getInfluxdbUser() + ":" + properties.getInfluxdbPassword();
    final var encoded = Base64.getEncoder().encodeToString(credentials.getBytes(Charsets.UTF_8));
    return WebClient
        .builder()
        .baseUrl(properties.getInfluxdbUrl())
        .defaultHeader("Authorization", "Basic " + encoded)
        .build();
  }
}
