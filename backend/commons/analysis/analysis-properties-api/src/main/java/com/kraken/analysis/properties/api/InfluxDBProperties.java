package com.kraken.analysis.properties.api;

public interface InfluxDBProperties extends UrlProperty, AuthenticationProperties {

  String getDatabase();
}