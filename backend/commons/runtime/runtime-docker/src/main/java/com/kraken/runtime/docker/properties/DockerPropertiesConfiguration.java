package com.kraken.runtime.docker.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
public class DockerPropertiesConfiguration {

  @Autowired
  @Bean
  DockerProperties dockerProperties(@Value("${kraken.docker.watch-tasks-delay:#{environment.KRAKEN_DOCKER_WATCH_TASKS_DELAY}}") final int delay) {
    return DockerProperties.builder()
        .watchTasksDelay(Duration.ofMillis(delay))
        .build();
  }

}
