package com.octoperf.kraken.config.influxdb.api;

import com.octoperf.kraken.config.api.AuthenticationProperties;
import com.octoperf.kraken.config.api.KrakenProperties;
import com.octoperf.kraken.config.api.UrlProperty;

public interface InfluxDBProperties extends UrlProperty, AuthenticationProperties, KrakenProperties {
  String getDatabase();
}