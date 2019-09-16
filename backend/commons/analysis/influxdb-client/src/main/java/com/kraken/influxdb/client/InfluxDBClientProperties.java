package com.kraken.influxdb.client;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class InfluxDBClientProperties {

  @NonNull
  String influxdbUrl;
  @NonNull
  String influxdbUser;
  @NonNull
  String influxdbPassword;
  @NonNull
  String influxdbDatabase;

}
