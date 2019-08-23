package com.kraken.influxdb.client;

import reactor.core.publisher.Mono;

public interface InfluxDBClient {
  Mono<String> deleteSeries(String testId);
}
