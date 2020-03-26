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
final class ImmutableApplicationProperties implements ApplicationProperties {
  @NonNull
  String data;

  @PostConstruct
  void log() {
    log.info(toString());
  }
}
