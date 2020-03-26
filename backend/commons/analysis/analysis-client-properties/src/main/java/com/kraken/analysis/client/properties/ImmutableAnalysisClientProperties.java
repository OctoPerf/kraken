package com.kraken.analysis.client.properties;

import com.kraken.analysis.client.properties.api.AnalysisClientProperties;
import com.kraken.tools.obfuscation.ExcludeFromObfuscation;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.annotation.PostConstruct;

@Slf4j
@Value
@Builder
@ConstructorBinding
@ExcludeFromObfuscation
@ConfigurationProperties("kraken.analysis")
final class ImmutableAnalysisClientProperties implements AnalysisClientProperties {
  @NonNull String url;

  @PostConstruct
  void log() {
    log.info(toString());
  }
}
