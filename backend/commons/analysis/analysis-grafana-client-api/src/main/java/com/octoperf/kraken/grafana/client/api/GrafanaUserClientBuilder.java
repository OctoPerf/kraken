package com.octoperf.kraken.grafana.client.api;

import com.octoperf.kraken.influxdb.client.api.InfluxDBUser;
import com.octoperf.kraken.tools.webclient.ClientBuilder;
import org.springframework.http.ResponseCookie;
import reactor.core.publisher.Mono;

public interface GrafanaUserClientBuilder {

  Mono<GrafanaUserClient> build(GrafanaUser grafanaUser, InfluxDBUser influxDBUser);

  Mono<ResponseCookie> getSessionCookie(GrafanaUser grafanaUser);
}
