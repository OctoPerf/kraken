package com.kraken.config.analysis.spring;

import com.kraken.config.grafana.api.AnalysisResultsProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.nio.file.Path;
import java.nio.file.Paths;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
@ConfigurationProperties("kraken.analysis.results")
final class SpringAnalysisResultsProperties implements AnalysisResultsProperties {
  @NonNull
  String root;

  @Override
  public Path getResultPath(final String resultId) {
    return Paths.get(root, resultId);
  }
}
