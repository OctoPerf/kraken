package com.kraken.grafana.client.properties;

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
@ConfigurationProperties(prefix = "kraken.grafana")
@ExcludeFromObfuscation
public class GrafanaClientProperties {
  @NonNull String url;
  @NonNull String dashboard;
  @NonNull String user;
  @NonNull String password;

  @PostConstruct
  void postConstruct() {
    log.info(toString());
  }
}
