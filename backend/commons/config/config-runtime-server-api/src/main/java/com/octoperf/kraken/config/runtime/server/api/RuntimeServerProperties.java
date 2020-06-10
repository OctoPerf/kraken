package com.octoperf.kraken.config.runtime.server.api;

import com.octoperf.kraken.config.api.KrakenProperties;

@FunctionalInterface
public interface RuntimeServerProperties extends KrakenProperties {

  String getConfigPath();
}
