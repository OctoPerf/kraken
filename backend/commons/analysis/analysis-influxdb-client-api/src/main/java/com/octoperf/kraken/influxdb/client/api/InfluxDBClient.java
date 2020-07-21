package com.octoperf.kraken.influxdb.client.api;

import com.octoperf.kraken.tools.webclient.Client;
import reactor.core.publisher.Mono;

public interface InfluxDBClient extends Client {

  Mono<String> deleteSeries(String database, String testId);

  Mono<InfluxDBUser> createUser(String userId);

  Mono<String> deleteUser(String userId);
}
