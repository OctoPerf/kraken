package com.kraken.runtime.container.properties;

import com.kraken.runtime.entity.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.kraken.tools.environment.KrakenEnvironmentAtValues.*;

@Slf4j
@Configuration
class RuntimeContainerPropertiesConfiguration {

  @Autowired
  @Bean
  RuntimeContainerProperties runtimeContainerProperties(
      @Value($KRAKEN_TASK_ID) final String taskId,
      @Value($KRAKEN_TASK_TYPE) final String taskType,
      @Value($KRAKEN_CONTAINER_NAME) final String containerName,
      @Value($KRAKEN_HOST_ID) final String hostId) {

    log.info("Task ID is set to " + taskId);
    log.info("Container Name is set to " + containerName);
    log.info("Host ID is set to " + hostId);

    return RuntimeContainerProperties.builder()
        .taskId(taskId)
        .taskType(TaskType.valueOf(taskType.toUpperCase()))
        .containerName(containerName)
        .hostId(hostId)
        .build();
  }

}

