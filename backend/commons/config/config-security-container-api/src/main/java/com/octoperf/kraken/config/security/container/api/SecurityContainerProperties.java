package com.octoperf.kraken.config.security.container.api;

import com.octoperf.kraken.config.api.KrakenProperties;

public interface SecurityContainerProperties extends KrakenProperties {
  String getAccessToken();

  String getRefreshToken();

  Long getRefreshExpiresIn();

  Long getExpiresIn();

  Long getMinValidity();

  Long getRefreshMinValidity();
}
