package com.kraken.runtime.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
class RuntimeClientConfiguration {

  @Autowired
  @Bean
  RuntimeClientProperties runtimeClientProperties(@Value("${kraken.runtime.url:#{environment.KRAKEN_RUNTIME_URL}}") final String runtimeUrl) {
    log.info("Storage URL is set to " + runtimeUrl);

    return RuntimeClientProperties.builder()
        .runtimeUrl(runtimeUrl)
        .build();
  }

  @Bean("webClientRuntime")
  @Autowired
  WebClient runtimeWebClient(final RuntimeClientProperties properties) {
    return WebClient
        .builder()
        .baseUrl(properties.getRuntimeUrl())
        .build();
  }

}
