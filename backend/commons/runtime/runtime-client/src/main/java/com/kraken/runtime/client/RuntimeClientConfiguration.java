package com.kraken.runtime.client;

import com.kraken.runtime.client.properties.RuntimeClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
class RuntimeClientConfiguration {

  @Bean("webClientRuntime")
  @Autowired
  WebClient runtimeWebClient(final RuntimeClientProperties properties) {
    return WebClient
        .builder()
        .baseUrl(properties.getRuntimeUrl())
        .build();
  }

}
