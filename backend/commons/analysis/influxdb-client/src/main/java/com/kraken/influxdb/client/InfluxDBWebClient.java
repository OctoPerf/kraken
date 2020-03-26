package com.kraken.influxdb.client;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Component
class InfluxDBWebClient implements InfluxDBClient {

  WebClient client;
  InfluxDBClientProperties influxdb;

  InfluxDBWebClient(
    final InfluxDBClientProperties influxdb,
    @Qualifier("webClientInfluxdb") final WebClient client) {
    super();
    this.client = requireNonNull(client);
    this.influxdb = requireNonNull(influxdb);
  }

  public Mono<String> deleteSeries(final String testId) {
    return client.post()
        .uri(uriBuilder -> uriBuilder.path("/query")
            .queryParam("db", influxdb.getDatabase())
            .build())
        .body(BodyInserters.fromFormData("q", String.format("DROP SERIES FROM /.*/ WHERE test = '%s'", testId)))
        .retrieve()
        .bodyToMono(String.class);
  }
}
