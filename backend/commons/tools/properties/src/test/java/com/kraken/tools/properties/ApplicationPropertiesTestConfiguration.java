package com.kraken.tools.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan
public class ApplicationPropertiesTestConfiguration {

  @Bean
  ApplicationProperties applicationProperties() {
    return ApplicationPropertiesTest.APPLICATION_PROPERTIES;
  }

}
