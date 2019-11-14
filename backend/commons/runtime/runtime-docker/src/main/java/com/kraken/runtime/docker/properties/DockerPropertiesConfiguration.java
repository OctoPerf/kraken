package com.kraken.runtime.docker.properties;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.TaskType;
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
  DockerProperties dockerProperties(@Value("${kraken.docker.containers-count.run:#{environment.KRAKEN_DOCKER_CONTAINERS_COUNT_RUN}}") final int runContainersCount,
                                    @Value("${kraken.docker.containers-count.debug:#{environment.KRAKEN_DOCKER_CONTAINERS_COUNT_DEBUG}}") final int debugContainersCount,
                                    @Value("${kraken.docker.containers-count.record:#{environment.KRAKEN_DOCKER_CONTAINERS_COUNT_RECORD}}") final int recordContainersCount) {
    return DockerProperties.builder()
        .containersCount(ImmutableMap.of(TaskType.RUN, runContainersCount,
            TaskType.DEBUG, debugContainersCount,
            TaskType.RECORD, recordContainersCount))
        .build();
  }

}
