package com.kraken.config.telegraf.spring;

import com.kraken.config.telegraf.api.TelegrafProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
@ConfigurationProperties("kraken.telegraf.conf")
class SpringTelegrafProperties implements TelegrafProperties {
  @NonNull String local;
  @NonNull String remote;
}

