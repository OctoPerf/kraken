package com.kraken.analysis.properties.api;

public interface InfluxDBClientProperties {

  String getUrl();

  String getUser();

  String getPassword();

  String getDatabase();
}