package com.kraken.tools.properties.api;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@Builder
@ConstructorBinding
@ConfigurationProperties("kraken.test")
public class TestProperties implements KrakenProperties {
  @NonNull String name;
}
