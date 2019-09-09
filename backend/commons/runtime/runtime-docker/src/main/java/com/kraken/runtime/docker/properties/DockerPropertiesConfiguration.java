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
  DockerProperties dockerProperties(@Value("${kraken.docker.watch-tasks-delay}") final int delay,
                                    @Value("${kraken.docker.containers-count.run}") final int runContainersCount,
                                    @Value("${kraken.docker.containers-count.debug}") final int debugContainersCount,
                                    @Value("${kraken.docker.containers-count.record}") final int recordContainersCount) {
    return DockerProperties.builder()
        .watchTasksDelay(Duration.ofMillis(delay))
        .containersCount(ImmutableMap.of(TaskType.RUN, runContainersCount,
            TaskType.DEBUG, debugContainersCount,
            TaskType.RECORD, recordContainersCount))
        .build();
  }

}
