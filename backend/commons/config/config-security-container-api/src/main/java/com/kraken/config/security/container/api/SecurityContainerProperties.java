package com.kraken.config.security.container.api;

import com.kraken.config.api.KrakenProperties;

import java.util.Optional;

public interface SecurityContainerProperties extends KrakenProperties {
  String getAccessToken();

  String getRefreshToken();

  Long getRefreshExpiresIn();

  Long getExpiresIn();

  Long getMinValidity();

  Long getRefreshMinValidity();
}
