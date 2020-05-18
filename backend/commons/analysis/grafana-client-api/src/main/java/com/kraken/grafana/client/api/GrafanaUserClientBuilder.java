package com.kraken.grafana.client.api;

import com.kraken.influxdb.client.api.InfluxDBUser;
import org.springframework.http.ResponseCookie;
import reactor.core.publisher.Mono;

public interface GrafanaUserClientBuilder {

  GrafanaUserClientBuilder grafanaUser(GrafanaUser user);

  GrafanaUserClientBuilder influxDBUser(InfluxDBUser user);

  Mono<GrafanaUserClient> build();

  Mono<ResponseCookie> getSessionCookie();
}
