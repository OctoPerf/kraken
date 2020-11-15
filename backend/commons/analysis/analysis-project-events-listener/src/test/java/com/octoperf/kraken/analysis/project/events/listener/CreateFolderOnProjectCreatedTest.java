package com.octoperf.kraken.analysis.project.events.listener;

import com.octoperf.kraken.grafana.client.api.GrafanaUserClient;
import com.octoperf.kraken.grafana.client.api.GrafanaUserClientBuilder;
import com.octoperf.kraken.grafana.client.api.GrafanaUserConverter;
import com.octoperf.kraken.grafana.client.api.GrafanaUserTest;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUserConverter;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUserTest;
import com.octoperf.kraken.project.event.CreateProjectEvent;
import com.octoperf.kraken.project.event.CreateProjectEventTest;
import com.octoperf.kraken.security.admin.client.api.SecurityAdminClient;
import com.octoperf.kraken.security.entity.owner.OwnerTest;
import com.octoperf.kraken.security.entity.user.KrakenUserTest;
import com.octoperf.kraken.tools.event.bus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateFolderOnProjectCreatedTest {

  @Mock
  EventBus eventBus;
  @Mock
  GrafanaUserClientBuilder grafanaUserClientBuilder;
  @Mock
  GrafanaUserClient grafanaUserClient;
  @Mock
  GrafanaUserConverter grafanaUserConverter;
  @Mock
  InfluxDBUserConverter influxDBUserConverter;
  @Mock
  SecurityAdminClient securityAdminClient;

  CreateFolderOnProjectCreated listener;

  @BeforeEach
  public void before() {
    given(eventBus.of(CreateProjectEvent.class)).willReturn(Flux.empty());
    listener = new CreateFolderOnProjectCreated(eventBus,
        grafanaUserClientBuilder,
        grafanaUserConverter,
        influxDBUserConverter,
        Mono.just(securityAdminClient));
  }

  @Test
  public void shouldHandleEvent() {
    final var event = CreateProjectEventTest.CREATE_PROJECT_EVENT.toBuilder().owner(OwnerTest.USER_OWNER).build();
    given(securityAdminClient.getUser(event.getOwner().getUserId())).willReturn(Mono.just(KrakenUserTest.KRAKEN_USER));
    given(grafanaUserConverter.apply(KrakenUserTest.KRAKEN_USER)).willReturn(GrafanaUserTest.GRAFANA_USER);
    given(influxDBUserConverter.apply(KrakenUserTest.KRAKEN_USER)).willReturn(InfluxDBUserTest.INFLUX_DB_USER);
    given(grafanaUserClientBuilder.build(GrafanaUserTest.GRAFANA_USER, InfluxDBUserTest.INFLUX_DB_USER)).willReturn(Mono.just(grafanaUserClient));
    given(grafanaUserClient.createFolder(event.getProject().getId(), event.getProject().getName())).willReturn(Mono.just(42L));
    listener.handleEvent(event);
    verify(grafanaUserClient).createFolder(event.getProject().getId(), event.getProject().getName());
  }

}