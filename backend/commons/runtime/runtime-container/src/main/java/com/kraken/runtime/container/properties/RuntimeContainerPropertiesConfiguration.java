package com.kraken.runtime.container.properties;

import com.kraken.runtime.entity.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
class RuntimeContainerPropertiesConfiguration {

  @Autowired
  @Bean
  RuntimeContainerProperties runtimeContainerProperties(
      @Value("${kraken.runtime.task.id:#{environment.KRAKEN_TASK_ID}}") final String taskId,
      @Value("${kraken.runtime.task.type:#{environment.KRAKEN_TASK_TYPE}}") final String taskType,
      @Value("${kraken.runtime.container.id:#{environment.KRAKEN_CONTAINER_ID}}") final String containerId,
      @Value("${kraken.runtime.host.id:#{environment.KRAKEN_HOST_ID}}") final String hostId,
      @Value("${kraken.runtime.host.name:#{environment.HOSTNAME}}") final String hostname) {

    // HOSTNAME is the docker container id when run on docker and the pod name when run on kubernetes

    log.info("Task ID is set to " + taskId);
    log.info("Container ID is set to " + containerId);

    return RuntimeContainerProperties.builder()
        .taskId(taskId)
        .taskType(TaskType.valueOf(taskType.toUpperCase()))
        .containerId(containerId)
        .hostId(hostId)
        .hostname(hostname)
        .build();
  }

}
