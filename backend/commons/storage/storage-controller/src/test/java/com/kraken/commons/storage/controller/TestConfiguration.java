package com.kraken.commons.storage.controller;

import com.kraken.commons.sse.SSEService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class TestConfiguration {

  @Bean
  SSEService sseService() {
    return Mockito.mock(SSEService.class);
  }
}