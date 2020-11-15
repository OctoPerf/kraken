package com.octoperf.kraken.analysis.user.events.listener;

import com.octoperf.kraken.influxdb.client.api.InfluxDBClient;
import com.octoperf.kraken.influxdb.client.api.InfluxDBClientBuilder;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUserAppender;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUserTest;
import com.octoperf.kraken.security.admin.client.api.SecurityAdminClient;
import com.octoperf.kraken.security.entity.user.KrakenUserTest;
import com.octoperf.kraken.grafana.client.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnalysisUserEventsServiceTest {

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
  InfluxDBClientBuilder influxDBClientBuilder;
  @Mock
  InfluxDBClient influxdbClient;
  @Mock
  InfluxDBUserAppender influxDBUserAppender;
  @Mock
  SecurityAdminClient securityAdminClient;

  private AnalysisUserEventsService service;

  @BeforeEach
  public void before() {
    service = new AnalysisUserEventsService(
        influxDBClientBuilder,
        influxDBUserAppender,
        grafanaUserClientBuilder,
        grafanaAdminClient,
        grafanaUserConverter,
        grafanaUserAppender,
        Mono.just(securityAdminClient)
    );
  }

  @Test
  public void shouldOnRegisterUser() {
    given(securityAdminClient.getUser(any())).willReturn(Mono.just(KrakenUserTest.KRAKEN_USER));
    given(grafanaUserClientBuilder.build(any(), any())).willReturn(Mono.just(grafanaUserClient));
    given(influxDBClientBuilder.build()).willReturn(Mono.just(influxdbClient));
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
    given(securityAdminClient.getUser(any())).willReturn(Mono.just(KrakenUserTest.KRAKEN_USER));
    given(grafanaUserConverter.apply(any())).willReturn(GrafanaUserTest.GRAFANA_USER);
    given(grafanaAdminClient.updateUser(any(), any(), any())).willReturn(Mono.just(GrafanaUserTest.GRAFANA_USER));

    assertThat(service.onUpdateEmail("userId", "updatedEmail", "previousEmail").block()).isEqualTo("userId");

    verify(grafanaAdminClient).updateUser(GrafanaUserTest.GRAFANA_USER, "userId", "updatedEmail");
  }

  @Test
  public void shouldOnDeleteUser() {
    given(grafanaAdminClient.deleteUser(any())).willReturn(Mono.just("userId"));
    given(influxDBClientBuilder.build()).willReturn(Mono.just(influxdbClient));
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
