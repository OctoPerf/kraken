package com.octoperf.kraken.config.backend.client.api;

import com.octoperf.kraken.config.api.KrakenProperties;
import com.octoperf.kraken.config.api.UrlProperty;

public interface BackendClientProperties extends UrlProperty, KrakenProperties {
  String getHostname();

  String getIp();
}
