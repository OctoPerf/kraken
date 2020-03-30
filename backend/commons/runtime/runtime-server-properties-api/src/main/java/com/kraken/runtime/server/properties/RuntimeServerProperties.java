package com.kraken.runtime.server.properties;

import com.kraken.tools.properties.api.KrakenProperties;

@FunctionalInterface
public interface RuntimeServerProperties extends KrakenProperties {

  String getConfigPath();
}
