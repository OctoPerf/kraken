package com.octoperf.kraken.grafana.client.api;

import com.octoperf.kraken.influxdb.client.api.InfluxDBUser;
import com.octoperf.kraken.tools.webclient.ClientBuilder;
import org.springframework.http.ResponseCookie;
import reactor.core.publisher.Mono;

public interface GrafanaUserClientBuilder extends ClientBuilder<GrafanaUserClient> {

  GrafanaUserClientBuilder grafanaUser(GrafanaUser user);

  GrafanaUserClientBuilder influxDBUser(InfluxDBUser user);

  Mono<ResponseCookie> getSessionCookie();
}
