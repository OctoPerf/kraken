package com.kraken.gatling.log.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GatlingParserTestConfiguration {

  @Bean
  GatlingParserProperties gatlingProperties() {
    return GatlingParserPropertiesTest.GATLING_PROPERTIES;
  }

}

