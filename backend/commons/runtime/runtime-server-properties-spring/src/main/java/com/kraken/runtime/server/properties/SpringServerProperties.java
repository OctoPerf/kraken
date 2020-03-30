package com.kraken.runtime.server.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import static com.google.common.base.Strings.nullToEmpty;

@Value
@Builder
@ConstructorBinding
@ConfigurationProperties("kraken.runtime.server")
final class SpringServerProperties implements RuntimeServerProperties {
  @NonNull
  String configPath;
}