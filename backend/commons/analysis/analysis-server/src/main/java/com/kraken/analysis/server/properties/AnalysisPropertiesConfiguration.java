package com.kraken.analysis.server.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AnalysisPropertiesConfiguration {

  @Autowired
  @Bean
  AnalysisProperties analysisProperties(@Value("${kraken.analysis.results.root:#{environment.KRAKEN_ANALYSIS_RESULTS_ROOT}}") final String resultsRoot) {
    log.info("Results root is set to " + resultsRoot);

    return AnalysisProperties.builder()
        .resultsRoot(resultsRoot)
        .build();
  }
}
