package com.kraken.telegraf;

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
@ConfigurationProperties("kraken.telegraf.conf")
class SpringTelegrafProperties implements TelegrafProperties {
  @NonNull String local;
  @NonNull String remote;
}

