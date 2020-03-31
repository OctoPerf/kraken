package com.kraken.config.runtime.client.api;

import com.kraken.config.api.KrakenProperties;

@FunctionalInterface
public interface RuntimeClientProperties extends KrakenProperties {

  String getUrl();
}
