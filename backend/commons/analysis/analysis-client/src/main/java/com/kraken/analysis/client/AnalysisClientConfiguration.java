package com.kraken.analysis.client;

import com.kraken.analysis.client.properties.AnalysisClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class AnalysisClientConfiguration {

  @Bean("webClientAnalysis")
  @Autowired
  WebClient analysisWebClient(final AnalysisClientProperties properties) {
    return WebClient
        .builder()
        .baseUrl(properties.getAnalysisUrl())
        .build();
  }
}
