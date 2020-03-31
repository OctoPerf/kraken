package com.kraken.config.grafana.api;

import com.kraken.config.api.AuthenticationProperties;
import com.kraken.config.api.KrakenProperties;
import com.kraken.config.api.UrlProperty;

public interface GrafanaProperties extends UrlProperty, AuthenticationProperties, KrakenProperties {

  String getDashboard();
}
