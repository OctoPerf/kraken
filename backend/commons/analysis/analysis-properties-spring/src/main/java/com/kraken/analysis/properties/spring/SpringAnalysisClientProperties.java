package com.kraken.analysis.properties.spring;

import com.kraken.analysis.properties.api.AnalysisClientProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@Builder
@ConstructorBinding
@ConfigurationProperties("kraken.analysis.client")
final class SpringAnalysisClientProperties implements AnalysisClientProperties {
  @NonNull String url;
}
