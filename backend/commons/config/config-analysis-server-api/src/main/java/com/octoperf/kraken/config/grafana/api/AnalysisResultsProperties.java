package com.octoperf.kraken.config.grafana.api;

import com.octoperf.kraken.config.api.KrakenProperties;

import java.nio.file.Path;

@FunctionalInterface
public interface AnalysisResultsProperties extends KrakenProperties {

  Path getResultPath(String resultId);
}
