package com.kraken.analysis.client.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.kraken.tools.environment.KrakenEnvironmentAtValues.$KRAKEN_ANALYSIS_URL;

@Slf4j
@Configuration
public class AnalysisClientPropertiesConfiguration {

  @Autowired
  @Bean
  AnalysisClientProperties analysisClientProperties(@Value($KRAKEN_ANALYSIS_URL) final String analysisUrl) {
    log.info("Analysis URL is set to " + analysisUrl);

    return AnalysisClientProperties.builder()
        .analysisUrl(analysisUrl)
        .build();
  }
}
