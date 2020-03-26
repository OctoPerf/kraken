package com.kraken.influxdb.client;

public interface InfluxDBClientProperties {

  String getUrl();

  String getUser();

  String getPassword();

  String getDatabase();
}
