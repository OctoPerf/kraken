package com.kraken.gatling.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GatlingRunnerTestConfiguration {

  @Bean
  GatlingRunnerProperties gatlingProperties() {
    return GatlingRunnerPropertiesTest.GATLING_PROPERTIES;
  }

}

