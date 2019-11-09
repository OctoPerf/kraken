package com.kraken.runtime.container.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuntimeContainerPropertiesTestConfiguration {

  @Bean
  RuntimeContainerProperties runtimeContainerProperties() {
    return RuntimeContainerPropertiesTest.RUNTIME_PROPERTIES;
  }

}
