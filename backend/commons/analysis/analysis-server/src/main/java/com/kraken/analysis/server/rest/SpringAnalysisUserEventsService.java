package com.kraken.analysis.server.rest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.grafana.client.api.*;
import com.kraken.influxdb.client.api.InfluxDBClient;
import com.kraken.influxdb.client.api.InfluxDBUser;
import com.kraken.influxdb.client.api.InfluxDBUserAppender;
import com.kraken.influxdb.client.api.InfluxDBUserConverter;
import com.kraken.security.admin.client.api.SecurityAdminClient;
import com.kraken.security.entity.user.KrakenUser;
import com.kraken.security.user.events.listener.UserEventsService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
class SpringAnalysisUserEventsService implements UserEventsService {

  @NonNull InfluxDBClient influxdbClient;
  @NonNull InfluxDBUserAppender influxDBUserAppender;
  @NonNull GrafanaUserClientBuilder grafanaUserClientBuilder;
  @NonNull GrafanaAdminClient grafanaAdminClient;
  @NonNull GrafanaUserConverter grafanaUserConverter;
  @NonNull GrafanaUserAppender grafanaUserAppender;
  @NonNull SecurityAdminClient securityAdminClient;

  @Override
  public Mono<String> onRegisterUser(final String userId, final String email, final String username) {
    final var createDBUser = influxdbClient.createUser(userId);
    final var createGrafanaUser = grafanaAdminClient.createUser(userId, email);
    return Mono.zip(createDBUser, createGrafanaUser)
        .flatMap(t2 -> Mono.zip(this.createDashboard(t2.getT2(), t2.getT1()), this.updateUser(userId, t2.getT2(), t2.getT1())))
        .map(objects -> userId);
  }

  private Mono<Long> createDashboard(final GrafanaUser grafanaUser, final InfluxDBUser influxDBUser) {
    return grafanaUserClientBuilder.influxDBUser(influxDBUser)
        .grafanaUser(grafanaUser)
        .build()
        .flatMap(GrafanaUserClient::createDatasource);
  }

  private Mono<KrakenUser> updateUser(final String userId, final GrafanaUser grafanaUser, final InfluxDBUser influxDBUser) {
    return securityAdminClient.getUser(userId)
        .map(krakenUser -> grafanaUserAppender.apply(influxDBUserAppender.apply(krakenUser, influxDBUser), grafanaUser))
        .flatMap(securityAdminClient::setUser);
  }

  @Override
  public Mono<String> onUpdateEmail(final String userId, final String updatedEmail, final String previousEmail) {
    return securityAdminClient.getUser(userId)
        .flatMap(krakenUser -> grafanaAdminClient.updateUser(grafanaUserConverter.apply(krakenUser), userId, updatedEmail))
        .map(grafanaUser -> userId);
  }

  @Override
  public Mono<String> onDeleteUser(final String userId) {
    return Mono.zip(influxdbClient.deleteUser(userId), grafanaAdminClient.deleteUser(userId))
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

