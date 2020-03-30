package com.kraken.influxdb.client;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface InfluxDBClient {

  Mono<String> deleteSeries(String testId);
}
