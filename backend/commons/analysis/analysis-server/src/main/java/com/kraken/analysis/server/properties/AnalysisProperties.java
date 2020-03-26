package com.kraken.analysis.server.properties;

import java.nio.file.Path;

@FunctionalInterface
public interface AnalysisProperties {

  Path getResultPath(String resultId);
}
