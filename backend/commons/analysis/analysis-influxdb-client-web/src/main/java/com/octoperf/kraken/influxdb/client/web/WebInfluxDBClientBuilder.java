package com.octoperf.kraken.influxdb.client.web;

import com.octoperf.kraken.config.influxdb.api.InfluxDBProperties;
import com.octoperf.kraken.influxdb.client.api.InfluxDBClient;
import com.octoperf.kraken.influxdb.client.api.InfluxDBClientBuilder;
import com.octoperf.kraken.tools.unique.id.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.octoperf.kraken.tools.webclient.Client.basicAuthorizationHeader;
import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor
final class WebInfluxDBClientBuilder implements InfluxDBClientBuilder {
  @NonNull InfluxDBProperties properties;
  @NonNull IdGenerator idGenerator;

  @Override
  public Mono<InfluxDBClient> build() {
    final var webClient = WebClient
        .builder()
        .baseUrl(properties.getUrl())
        .defaultHeader(HttpHeaders.AUTHORIZATION, basicAuthorizationHeader(properties.getUser(), properties.getPassword()))
        .build();
    return Mono.just(new WebInfluxDBClient(idGenerator, webClient));
  }
}
