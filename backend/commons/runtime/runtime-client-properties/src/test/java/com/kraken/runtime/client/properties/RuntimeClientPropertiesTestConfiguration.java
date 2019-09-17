package com.kraken.runtime.client.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RuntimeClientPropertiesTestConfiguration {

  @Bean
  RuntimeClientProperties properties() {
    return RuntimeClientPropertiesTest.RUNTIME_PROPERTIES;
  }

}