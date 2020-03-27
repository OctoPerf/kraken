package com.kraken.analysis.properties.api;

import com.kraken.tools.properties.api.AuthenticationProperties;
import com.kraken.tools.properties.api.KrakenProperties;
import com.kraken.tools.properties.api.UrlProperty;

public interface GrafanaProperties extends UrlProperty, AuthenticationProperties, KrakenProperties {

  String getDashboard();
}
