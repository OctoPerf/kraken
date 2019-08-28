package com.kraken.analysis.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class AnalysisClientConfiguration {

  @Autowired
  @Bean
  AnalysisClientProperties influxDBClientProperties(@Value("${kraken.analysis.url:#{environment.KRAKEN_URLS_ANALYSIS}}") final String analysisUrl) {
    log.info("Analysis URL is set to " + analysisUrl);

    return AnalysisClientProperties.builder()
        .analysisUrl(analysisUrl)
        .build();
  }

  @Bean("webClientAnalysis")
  @Autowired
  WebClient analysisWebClient(final AnalysisClientProperties properties) {
    return WebClient
        .builder()
        .baseUrl(properties.getAnalysisUrl())
        .build();
  }
}
