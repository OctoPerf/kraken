package com.kraken.commons.rest.configuration;

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
    return ApplicationProperties.builder().data(Path.of("testDir"))
        .hostData(Path.of("testDir").toAbsolutePath().toString())
        .hostUId("1001")
        .hostGId("1001")
        .build();
  }

}
