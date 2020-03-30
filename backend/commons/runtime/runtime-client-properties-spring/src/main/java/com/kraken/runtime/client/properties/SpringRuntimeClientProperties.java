package com.kraken.runtime.client.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@Builder
@ConstructorBinding
@ConfigurationProperties("kraken.runtime")
final class SpringRuntimeClientProperties implements RuntimeClientProperties {
  @NonNull String url;
}
