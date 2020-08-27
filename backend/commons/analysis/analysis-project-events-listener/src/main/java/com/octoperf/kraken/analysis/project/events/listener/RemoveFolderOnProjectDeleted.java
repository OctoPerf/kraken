package com.octoperf.kraken.analysis.project.events.listener;

import com.octoperf.kraken.grafana.client.api.GrafanaUserClientBuilder;
import com.octoperf.kraken.grafana.client.api.GrafanaUserConverter;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUserConverter;
import com.octoperf.kraken.project.event.CreateProjectEvent;
import com.octoperf.kraken.project.event.DeleteProjectEvent;
import com.octoperf.kraken.security.admin.client.api.SecurityAdminClient;
import com.octoperf.kraken.tools.event.bus.EventBus;
import com.octoperf.kraken.tools.event.bus.EventBusListener;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class RemoveFolderOnProjectDeleted extends EventBusListener<DeleteProjectEvent> {

  GrafanaUserClientBuilder grafanaUserClientBuilder;
  GrafanaUserConverter grafanaUserConverter;
  InfluxDBUserConverter influxDBUserConverter;
  Mono<SecurityAdminClient> securityAdminClient;

  @Autowired
  RemoveFolderOnProjectDeleted(final EventBus eventBus,
                               @NonNull final GrafanaUserClientBuilder grafanaUserClientBuilder,
                               @NonNull final GrafanaUserConverter grafanaUserConverter,
                               @NonNull final InfluxDBUserConverter influxDBUserConverter,
                               @NonNull final Mono<SecurityAdminClient> securityAdminClient) {
    super(eventBus, DeleteProjectEvent.class);
    this.grafanaUserClientBuilder = grafanaUserClientBuilder;
    this.grafanaUserConverter = grafanaUserConverter;
    this.influxDBUserConverter = influxDBUserConverter;
    this.securityAdminClient = securityAdminClient;
  }

  @Override
  protected void handleEvent(final DeleteProjectEvent event) {
    securityAdminClient.flatMap(client -> client.getUser(event.getOwner().getUserId())
        .flatMap(krakenUser -> this.grafanaUserClientBuilder.build(this.grafanaUserConverter.apply(krakenUser), this.influxDBUserConverter.apply(krakenUser)))
        .flatMap(grafanaUserClient -> grafanaUserClient.deleteFolder(event.getProject().getId())))
        .subscribe();
  }
}
