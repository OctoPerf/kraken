package com.kraken.debug.entry.writer;

import com.kraken.analysis.client.AnalysisClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
class TestConfiguration {

  @Bean
  AnalysisClient client() {
    return Mockito.mock(AnalysisClient.class);
  }
}