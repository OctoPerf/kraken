package com.kraken.storage.server;

import com.kraken.tools.sse.SSEService;
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