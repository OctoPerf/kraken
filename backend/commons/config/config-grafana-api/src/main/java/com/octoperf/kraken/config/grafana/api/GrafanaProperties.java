package com.octoperf.kraken.config.grafana.api;

import com.octoperf.kraken.config.api.AuthenticationProperties;
import com.octoperf.kraken.config.api.KrakenProperties;
import com.octoperf.kraken.config.api.UrlProperty;

public interface GrafanaProperties extends UrlProperty, AuthenticationProperties, KrakenProperties {
  String getDashboard();
}
