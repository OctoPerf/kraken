package com.kraken.runtime.server.properties;

import com.kraken.tools.properties.api.KrakenProperties;

@FunctionalInterface
public interface ServerProperties extends KrakenProperties {

  String getConfigPath();
}
