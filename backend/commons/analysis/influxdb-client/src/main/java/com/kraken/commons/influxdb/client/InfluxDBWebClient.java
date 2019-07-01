package com.kraken.commons.influxdb.client;

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

  WebClient webClient;
  InfluxDBClientProperties properties;

  InfluxDBWebClient(final InfluxDBClientProperties properties, @Qualifier("webClientInfluxdb") final WebClient webClient) {
    this.webClient = requireNonNull(webClient);
    this.properties = requireNonNull(properties);
  }

  public Mono<String> deleteSeries(final String testId) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/query")
            .queryParam("db", properties.getInfluxdbDatabase())
            .build())
        .body(BodyInserters.fromFormData("q", String.format("DROP SERIES FROM /.*/ WHERE test = '%s'", testId)))
        .retrieve()
        .bodyToMono(String.class);
  }
}
