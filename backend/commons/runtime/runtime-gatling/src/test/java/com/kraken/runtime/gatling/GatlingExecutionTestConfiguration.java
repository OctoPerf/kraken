package com.kraken.runtime.gatling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GatlingExecutionTestConfiguration {

  @Bean
  GatlingExecutionProperties gatlingProperties() {
    return GatlingExecutionPropertiesTest.GATLING_PROPERTIES;
  }

}

