package com.kraken.gatling.properties.spring;

import com.kraken.gatling.properties.api.GatlingProperties;
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
@ConfigurationProperties("kraken.gatling")
final class ImmutableGatlingProperties implements GatlingProperties {
  @NonNull String home;
  @NonNull String debugLog;

  @PostConstruct
  void log() {
    log.info(toString());
  }
}

