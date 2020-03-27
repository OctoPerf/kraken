package com.kraken.analysis.properties.api;

import com.kraken.tools.properties.api.KrakenProperties;

import java.nio.file.Path;

@FunctionalInterface
public interface AnalysisResultsProperties extends KrakenProperties {

  Path getResultPath(String resultId);
}
