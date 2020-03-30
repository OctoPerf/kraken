package com.kraken.runtime.client.properties;

import com.kraken.tools.properties.api.KrakenProperties;

@FunctionalInterface
public interface RuntimeClientProperties extends KrakenProperties {

  String getUrl();
}
