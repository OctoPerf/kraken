package com.kraken.analysis.properties.api;

import java.nio.file.Path;

@FunctionalInterface
public interface AnalysisResultsProperties {

  Path getResultPath(String resultId);
}
