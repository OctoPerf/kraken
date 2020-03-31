package com.kraken.influxdb.client;

import com.google.common.base.Charsets;
import com.kraken.config.influxdb.api.InfluxDBProperties;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Component
class InfluxDBWebClient implements InfluxDBClient {

  WebClient client;
  InfluxDBProperties influxdb;

  InfluxDBWebClient(final InfluxDBProperties properties) {
    super();
    final var credentials = properties.getUser() + ":" + properties.getPassword();
    final var encoded = Base64.getEncoder().encodeToString(credentials.getBytes(Charsets.UTF_8));
    this.client = WebClient
      .builder()
      .baseUrl(properties.getUrl())
      .defaultHeader("Authorization", "Basic " + encoded)
      .build();
    this.influxdb = requireNonNull(properties);
  }

  public Mono<String> deleteSeries(final String testId) {
    return client.post()
        .uri(uri -> uri.path("/query").queryParam("db", influxdb.getDatabase()).build())
        .body(fromFormData("q", format("DROP SERIES FROM /.*/ WHERE test = '%s'", testId)))
        .retrieve()
        .bodyToMono(String.class);
  }
}
