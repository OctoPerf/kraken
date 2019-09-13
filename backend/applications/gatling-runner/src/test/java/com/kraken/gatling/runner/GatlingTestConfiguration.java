package com.kraken.gatling.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GatlingTestConfiguration {

  @Bean
  GatlingProperties gatlingProperties() {
    return GatlingPropertiesTest.GATLING_PROPERTIES;
  }


}

