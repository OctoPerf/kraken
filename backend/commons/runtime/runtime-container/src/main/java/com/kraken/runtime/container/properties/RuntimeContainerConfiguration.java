package com.kraken.runtime.container.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
class RuntimeContainerConfiguration {

  @Autowired
  @Bean
  RuntimeContainerProperties runtimeContainerProperties(
      @Value("${kraken.runtime.task.id:#{environment.KRAKEN_TASK_ID}}") final String taskId,
      @Value("${kraken.runtime.container.id:#{environment.KRAKEN_CONTAINER_ID}}") final String containerId) {

    log.info("Task ID is set to " + taskId);
    log.info("Container ID is set to " + containerId);

    return RuntimeContainerProperties.builder()
        .taskId(taskId)
        .containerId(containerId)
        .build();
  }

}
