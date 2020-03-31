package com.kraken.config.influxdb.api;

import com.kraken.config.api.AuthenticationProperties;
import com.kraken.config.api.KrakenProperties;
import com.kraken.config.api.UrlProperty;

public interface InfluxDBProperties extends UrlProperty, AuthenticationProperties, KrakenProperties {

  String getDatabase();
}