package com.kraken.commons.gatling.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;
import java.nio.file.Paths;

@Value
@Builder
public class GatlingProperties {

  @NonNull
  String resultsRoot;
  @NonNull
  String version;

  public Path getTestResultPath(final String testId) {
    return Paths.get(this.resultsRoot, testId);
  }
}
