package com.kraken.tools.properties;

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
@ConfigurationProperties("kraken")
final class ImmutableKrakenProperties implements KrakenProperties {
  @NonNull
  String data;
  @NonNull
  String version;

  @PostConstruct
  void log() {
    log.info(toString());
  }
}
