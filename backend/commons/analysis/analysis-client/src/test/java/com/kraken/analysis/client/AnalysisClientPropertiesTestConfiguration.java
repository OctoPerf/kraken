package com.kraken.analysis.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalysisClientPropertiesTestConfiguration {

  @Bean
  AnalysisClientProperties analysisProperties() {
    return AnalysisClientPropertiesTest.ANALYSIS_CLIENT_PROPERTIES;
  }

}
