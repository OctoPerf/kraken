package com.kraken.config.runtime.client.spring;

import com.kraken.config.runtime.client.api.RuntimeClientProperties;
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
