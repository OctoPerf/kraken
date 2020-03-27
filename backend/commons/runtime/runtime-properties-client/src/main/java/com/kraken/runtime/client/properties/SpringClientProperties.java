package com.kraken.runtime.client.properties;

import com.kraken.tools.obfuscation.ExcludeFromObfuscation;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@Builder
@ConstructorBinding
@ExcludeFromObfuscation
@ConfigurationProperties("kraken.client")
final class SpringClientProperties implements ClientProperties {
  @NonNull String url;
}
