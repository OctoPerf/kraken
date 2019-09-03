package com.kraken.tools.configuration.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Slf4j
@Configuration
@ComponentScan
public class ApplicationPropertiesTestConfiguration {

  @Bean
  ApplicationProperties applicationProperties() {
    return ApplicationPropertiesTest.APPLICATION_PROPERTIES;
  }

}
