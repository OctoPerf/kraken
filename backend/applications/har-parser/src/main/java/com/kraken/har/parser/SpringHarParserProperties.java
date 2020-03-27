package com.kraken.har.parser;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@Builder
@ConstructorBinding
@ConfigurationProperties("kraken.gatling.har-parser")
public class SpringHarParserProperties implements HarParserProperties {
  @NonNull String local;
  @NonNull String remote;
}