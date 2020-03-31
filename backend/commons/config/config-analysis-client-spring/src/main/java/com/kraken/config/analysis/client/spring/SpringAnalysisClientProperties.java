package com.kraken.config.analysis.client.spring;

import com.kraken.config.analysis.client.api.AnalysisClientProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@Builder
@ConstructorBinding
@ConfigurationProperties("kraken.analysis")
final class SpringAnalysisClientProperties implements AnalysisClientProperties {
  @NonNull String url;
}
