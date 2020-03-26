package com.kraken.analysis.properties.spring;

import com.kraken.analysis.properties.api.GrafanaClientProperties;
import com.kraken.tools.obfuscation.ExcludeFromObfuscation;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.annotation.PostConstruct;

@Slf4j
@Value
@Builder
@ConstructorBinding
@ExcludeFromObfuscation
@ConfigurationProperties(prefix = "kraken.grafana")
final class ImmutableGrafanaClientProperties implements GrafanaClientProperties {
  @NonNull String url;
  @NonNull String dashboard;
  @NonNull String user;
  @NonNull String password;

  @PostConstruct
  void log() {
    log.info(toString());
  }
}
