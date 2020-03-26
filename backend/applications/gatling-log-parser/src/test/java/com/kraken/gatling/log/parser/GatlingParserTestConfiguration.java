package com.kraken.gatling.log.parser;

import com.kraken.gatling.properties.api.GatlingProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GatlingParserTestConfiguration {

  @Bean
  GatlingProperties gatlingProperties() {
    return GatlingPropertiesTest.GATLING_PROPERTIES;
  }

}

