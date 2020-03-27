package com.kraken.runtime.server.properties;

import lombok.Builder;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import static com.google.common.base.Strings.nullToEmpty;

@Value
@Builder
@ConstructorBinding
@ConfigurationProperties("kraken.server")
final class SpringServerProperties implements ServerProperties {
  String configPath;

  SpringServerProperties(final String configPath) {
    super();
    this.configPath = nullToEmpty(configPath);
  }
}