package com.kraken.analysis.properties.api;

public interface GrafanaProperties extends UrlProperty, AuthenticationProperties {

  String getDashboard();
}
