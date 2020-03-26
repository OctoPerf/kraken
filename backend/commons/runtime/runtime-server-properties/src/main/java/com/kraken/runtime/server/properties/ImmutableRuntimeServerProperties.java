package com.kraken.runtime.server.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Slf4j
@Value
@Builder
@ConstructorBinding
@ConfigurationProperties("kraken.runtime")
public class ImmutableRuntimeServerProperties implements RuntimeServerProperties {
  @NonNull
  String version;
  @NonNull
  String configurationPath;
}