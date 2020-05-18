package com.kraken.config.har.parser.spring;

import com.kraken.config.har.parser.api.HarParserProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
@ConfigurationProperties("kraken.har-parser")
public class SpringHarParserProperties implements HarParserProperties {
  @NonNull String local;
  @NonNull String remote;
}