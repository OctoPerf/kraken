package com.kraken.config.runtime.server.api;

import com.kraken.config.api.KrakenProperties;

@FunctionalInterface
public interface RuntimeServerProperties extends KrakenProperties {

  String getConfigPath();
}
