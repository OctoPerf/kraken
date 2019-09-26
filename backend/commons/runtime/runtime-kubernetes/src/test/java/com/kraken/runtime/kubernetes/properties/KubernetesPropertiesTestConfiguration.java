package com.kraken.runtime.docker.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DockerPropertiesTestConfiguration {

  @Bean
  DockerProperties properties() {
    return DockerPropertiesTest.DOCKER_PROPERTIES;
  }

}
