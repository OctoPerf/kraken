package com.octoperf.kraken.analysis.user.events.listener;

import com.octoperf.kraken.influxdb.client.api.InfluxDBClientBuilder;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUser;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUserAppender;
import com.octoperf.kraken.security.admin.client.api.SecurityAdminClient;
import com.octoperf.kraken.security.entity.user.KrakenUser;
import com.octoperf.kraken.security.user.events.listener.UserEventsService;
import com.octoperf.kraken.grafana.client.api.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class AnalysisUserEventsService implements UserEventsService {

  @NonNull InfluxDBClientBuilder influxDBClientBuilder;
  @NonNull InfluxDBUserAppender influxDBUserAppender;
  @NonNull GrafanaUserClientBuilder grafanaUserClientBuilder;
  @NonNull GrafanaAdminClient grafanaAdminClient;
  @NonNull GrafanaUserConverter grafanaUserConverter;
  @NonNull GrafanaUserAppender grafanaUserAppender;
  @NonNull Mono<SecurityAdminClient> securityAdminClientMono;

  @Override
  public Mono<String> onRegisterUser(final String userId, final String email, final String username) {
    final var createDBUser = influxDBClientBuilder.build().flatMap(client -> client.createUser(userId));
    final var createGrafanaUser = grafanaAdminClient.createUser(userId, email);
    return Mono.zip(createDBUser, createGrafanaUser)
        .flatMap(t2 -> Mono.zip(this.createDataSource(t2.getT2(), t2.getT1()), this.updateUser(userId, t2.getT2(), t2.getT1())))
        .map(objects -> userId);
  }

  private Mono<Long> createDataSource(final GrafanaUser grafanaUser, final InfluxDBUser influxDBUser) {
    return grafanaUserClientBuilder.build(grafanaUser, influxDBUser)
        .flatMap(GrafanaUserClient::createDatasource);
  }

  private Mono<KrakenUser> updateUser(final String userId, final GrafanaUser grafanaUser, final InfluxDBUser influxDBUser) {
    return securityAdminClientMono.flatMap(securityAdminClient -> securityAdminClient.getUser(userId)
        .map(krakenUser -> grafanaUserAppender.apply(influxDBUserAppender.apply(krakenUser, influxDBUser), grafanaUser))
        .flatMap(securityAdminClient::setUser));
  }

  @Override
  public Mono<String> onUpdateEmail(final String userId, final String updatedEmail, final String previousEmail) {
    return securityAdminClientMono.flatMap(securityAdminClient -> securityAdminClient.getUser(userId)
        .flatMap(krakenUser -> grafanaAdminClient.updateUser(grafanaUserConverter.apply(krakenUser), userId, updatedEmail))
        .map(grafanaUser -> userId));
  }

  @Override
  public Mono<String> onDeleteUser(final String userId) {
    return Mono.zip(influxDBClientBuilder.build().flatMap(client -> client.deleteUser(userId)), grafanaAdminClient.deleteUser(userId))
        .map(t2 -> userId);
  }

  @Override
  public Mono<String> onCreateRole(final String userId, final String role) {
    return Mono.just(userId);
  }

  @Override
  public Mono<String> onDeleteRole(final String userId, final String role) {
    return Mono.just(userId);
  }

}

