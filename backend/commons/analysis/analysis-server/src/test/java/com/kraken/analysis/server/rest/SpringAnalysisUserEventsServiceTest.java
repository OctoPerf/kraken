package com.kraken.analysis.server.rest;

import com.kraken.grafana.client.api.*;
import com.kraken.influxdb.client.api.InfluxDBClient;
import com.kraken.influxdb.client.api.InfluxDBUserAppender;
import com.kraken.influxdb.client.api.InfluxDBUserConverter;
import com.kraken.influxdb.client.api.InfluxDBUserTest;
import com.kraken.security.admin.client.api.SecurityAdminClient;
import com.kraken.security.entity.user.KrakenUserTest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SpringAnalysisUserEventsServiceTest {

  @Mock
  GrafanaUserClientBuilder grafanaUserClientBuilder;
  @Mock
  GrafanaUserClient grafanaUserClient;
  @Mock
  GrafanaAdminClient grafanaAdminClient;
  @Mock
  GrafanaUserConverter grafanaUserConverter;
  @Mock
  GrafanaUserAppender grafanaUserAppender;
  @Mock
  InfluxDBClient influxdbClient;
  @Mock
  InfluxDBUserAppender influxDBUserAppender;
  @Mock
  SecurityAdminClient securityAdminClient;

  private SpringAnalysisUserEventsService service;

  @Before
  public void before() {
    given(securityAdminClient.getUser(any())).willReturn(Mono.just(KrakenUserTest.KRAKEN_USER));
    given(grafanaUserConverter.apply(any())).willReturn(GrafanaUserTest.GRAFANA_USER);
    given(grafanaUserClientBuilder.grafanaUser(any())).willReturn(grafanaUserClientBuilder);
    given(grafanaUserClientBuilder.influxDBUser(any())).willReturn(grafanaUserClientBuilder);
    given(grafanaUserClientBuilder.build()).willReturn(Mono.just(grafanaUserClient));

    service = new SpringAnalysisUserEventsService(
        influxdbClient,
        influxDBUserAppender,
        grafanaUserClientBuilder,
        grafanaAdminClient,
        grafanaUserConverter,
        grafanaUserAppender,
        securityAdminClient
    );
  }

  @Test
  public void shouldOnRegisterUser() {
    given(influxdbClient.createUser(anyString())).willReturn(Mono.just(InfluxDBUserTest.INFLUX_DB_USER));
    given(grafanaAdminClient.createUser(anyString(), anyString())).willReturn(Mono.just(GrafanaUserTest.GRAFANA_USER));
    given(grafanaUserClient.createDatasource()).willReturn(Mono.just(42L));
    given(securityAdminClient.setUser(any())).willReturn(Mono.just(KrakenUserTest.KRAKEN_USER));
    given(grafanaUserAppender.apply(any(), any())).willReturn(KrakenUserTest.KRAKEN_USER);
    given(influxDBUserAppender.apply(any(), any())).willReturn(KrakenUserTest.KRAKEN_USER);

    assertThat(service.onRegisterUser("userId", "email", "username").block()).isEqualTo("userId");

    verify(securityAdminClient).setUser(KrakenUserTest.KRAKEN_USER);
  }

  @Test
  public void shouldOnUpdateEmail() {
    given(grafanaAdminClient.updateUser(any(), any(), any())).willReturn(Mono.just(GrafanaUserTest.GRAFANA_USER));

    assertThat(service.onUpdateEmail("userId", "updatedEmail", "previousEmail").block()).isEqualTo("userId");

    verify(grafanaAdminClient).updateUser(GrafanaUserTest.GRAFANA_USER, "userId", "updatedEmail");
  }

  @Test
  public void shouldOnDeleteUser() {
    given(grafanaAdminClient.deleteUser(any())).willReturn(Mono.just("userId"));
    given(influxdbClient.deleteUser(any())).willReturn(Mono.just("userId"));

    assertThat(service.onDeleteUser("userId").block()).isEqualTo("userId");

    verify(grafanaAdminClient).deleteUser("userId");
    verify(influxdbClient).deleteUser("userId");
  }

  @Test
  public void shouldOnCreateRole() {
    assertThat(service.onCreateRole("userId", "ADMIN").block()).isEqualTo("userId");
  }

  @Test
  public void shouldOnDeleteRole() {
    assertThat(service.onCreateRole("userId", "ADMIN").block()).isEqualTo("userId");
  }
}
