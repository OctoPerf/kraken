package com.kraken.analysis.server.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.nio.file.Path;
import java.nio.file.Paths;

@Value
@Builder
@ConstructorBinding
@ConfigurationProperties("kraken.analysis.results")
public class ImmutableAnalysisProperties implements AnalysisProperties {

  @NonNull
  String root;

  @Override
  public Path getResultPath(final String resultId) {
    return Paths.get(root, resultId);
  }
}
