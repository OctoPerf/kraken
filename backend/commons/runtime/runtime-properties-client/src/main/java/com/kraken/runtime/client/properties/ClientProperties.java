package com.kraken.runtime.client.properties;

import com.kraken.tools.properties.api.KrakenProperties;

@FunctionalInterface
public interface ClientProperties extends KrakenProperties {

  String getUrl();
}
