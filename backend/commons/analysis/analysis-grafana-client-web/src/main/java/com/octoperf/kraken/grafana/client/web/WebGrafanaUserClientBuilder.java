package com.octoperf.kraken.grafana.client.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octoperf.kraken.config.grafana.api.GrafanaProperties;
import com.octoperf.kraken.config.influxdb.api.InfluxDBProperties;
import com.octoperf.kraken.grafana.client.api.GrafanaUser;
import com.octoperf.kraken.grafana.client.api.GrafanaUserClient;
import com.octoperf.kraken.grafana.client.api.GrafanaUserClientBuilder;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUser;
import com.octoperf.kraken.security.entity.user.KrakenUser;
import com.octoperf.kraken.template.api.TemplateService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
class WebGrafanaUserClientBuilder implements GrafanaUserClientBuilder {

  static final String GRAFANA_SESSION = "grafana_session";

  @NonNull ObjectMapper mapper;
  @NonNull GrafanaProperties grafanaProperties;
  @NonNull InfluxDBProperties dbProperties;
  @NonNull TemplateService templateService;

  @Override
  public Mono<GrafanaUserClient> build(final GrafanaUser grafanaUser, final InfluxDBUser influxDBUser) {
    return getSessionCookie(grafanaUser)
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
  public Mono<ResponseCookie> getSessionCookie(final GrafanaUser grafanaUser) {
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
