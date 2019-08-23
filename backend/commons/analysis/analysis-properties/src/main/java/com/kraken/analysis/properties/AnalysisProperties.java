package com.kraken.analysis.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;
import java.nio.file.Paths;

@Value
@Builder
public class AnalysisProperties {

  @NonNull
  String resultsRoot;

  public Path getResultPath(final String resultId) {
    return Paths.get(this.resultsRoot, resultId);
  }
}
