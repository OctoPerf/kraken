package com.kraken.runtime.container.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuntimeContainerTestConfiguration {

  @Bean
  RuntimeContainerProperties runtimeContainerProperties() {
    return RuntimeContainerPropertiesTest.RUNTIME_PROPERTIES;
  }

}
