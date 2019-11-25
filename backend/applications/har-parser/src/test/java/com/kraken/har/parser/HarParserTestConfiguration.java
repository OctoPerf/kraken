package com.kraken.har.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class HarParserTestConfiguration {

  @Bean
  HarParserProperties properties() {
    return HarParserPropertiesTest.HAR_PROPERTIES;
  }

}

