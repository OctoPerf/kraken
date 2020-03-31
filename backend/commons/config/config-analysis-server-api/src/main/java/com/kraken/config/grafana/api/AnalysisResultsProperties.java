package com.kraken.config.grafana.api;

import com.kraken.config.api.KrakenProperties;

import java.nio.file.Path;

@FunctionalInterface
public interface AnalysisResultsProperties extends KrakenProperties {

  Path getResultPath(String resultId);
}
