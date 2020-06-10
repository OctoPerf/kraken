package com.octoperf.kraken.grafana.client.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octoperf.kraken.config.grafana.api.GrafanaProperties;
import com.octoperf.kraken.config.influxdb.api.InfluxDBProperties;
import com.octoperf.kraken.grafana.client.api.GrafanaUser;
import com.octoperf.kraken.grafana.client.api.GrafanaUserClient;
import com.octoperf.kraken.grafana.client.api.GrafanaUserClientBuilder;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUser;
import com.octoperf.kraken.template.api.TemplateService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
class WebGrafanaUserClientBuilder implements GrafanaUserClientBuilder {

  static final String GRAFANA_SESSION = "grafana_session";

  GrafanaUser grafanaUser;
  InfluxDBUser influxDBUser;
  final ObjectMapper mapper;
  final GrafanaProperties grafanaProperties;
  final InfluxDBProperties dbProperties;
  final TemplateService templateService;

  public WebGrafanaUserClientBuilder(@NonNull final ObjectMapper mapper,
                                     @NonNull final GrafanaProperties grafanaProperties,
                                     @NonNull final InfluxDBProperties dbProperties,
                                     @NonNull final TemplateService templateService) {
    this.mapper = mapper;
    this.grafanaProperties = grafanaProperties;
    this.dbProperties = dbProperties;
    this.templateService = templateService;
  }


  @Override
  public GrafanaUserClientBuilder grafanaUser(final GrafanaUser grafanaUser) {
    this.grafanaUser = grafanaUser;
    return this;
  }

  @Override
  public GrafanaUserClientBuilder influxDBUser(final InfluxDBUser influxDBUser) {
    this.influxDBUser = influxDBUser;
    return this;
  }

  @Override
  public Mono<GrafanaUserClient> build() {
    return getSessionCookie()
        .map(sessionCookie -> new WebGrafanaUserClient(grafanaUser,
            influxDBUser,
            WebClient
                .builder()
                .baseUrl(grafanaProperties.getUrl())
                .defaultCookie(GRAFANA_SESSION, sessionCookie.getValue())
                .build(),
            dbProperties,
            mapper,
            templateService));
  }

  @Override
  public Mono<ResponseCookie> getSessionCookie() {
    final var loginClient = WebClient
        .builder()
        .baseUrl(grafanaProperties.getUrl())
        .build();
    return loginClient.post()
        .uri(uriBuilder -> uriBuilder.path("/login").build())
        .body(BodyInserters.fromValue(LoginRequest.builder()
            .email(grafanaUser.getEmail())
            .password(grafanaUser.getPassword())
            .user(grafanaUser.getEmail())
            .build()))
        .exchange()
        .map(response -> response.cookies().getFirst(GRAFANA_SESSION));
  }
}
