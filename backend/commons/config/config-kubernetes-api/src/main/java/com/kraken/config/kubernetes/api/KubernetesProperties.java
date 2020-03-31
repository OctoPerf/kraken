package com.kraken.config.kubernetes.api;

import com.kraken.config.api.KrakenProperties;

public interface KubernetesProperties extends KrakenProperties {

  String getNamespace();

  boolean isDebug();

  boolean isPatchHosts();

  String getPretty();

  KubernetesClientBuilderType getBuilderType();

  java.util.Optional<String> getConfigPath();
}
