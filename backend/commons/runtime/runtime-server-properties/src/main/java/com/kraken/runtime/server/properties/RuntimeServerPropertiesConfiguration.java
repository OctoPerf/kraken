package com.kraken.runtime.server.properties;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.ResourcesAllocation;
import com.kraken.runtime.entity.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.kraken.tools.environment.KrakenEnvironmentAtValues.*;

@Slf4j
@Configuration
public class RuntimeServerPropertiesConfiguration {

  @Autowired
  @Bean
  RuntimeServerProperties runtimeServerProperties(@Value($KRAKEN_RUNTIME_CONTAINERS_COUNT_RUN) final int runContainersCount,
                                                  @Value($KRAKEN_RUNTIME_CONTAINERS_COUNT_DEBUG) final int debugContainersCount,
                                                  @Value($KRAKEN_RUNTIME_CONTAINERS_COUNT_RECORD) final int recordContainersCount,
                                                  @Value($KRAKEN_RUNTIME_CPU_REQUEST) final String cpuRequest,
                                                  @Value($KRAKEN_RUNTIME_CPU_LIMIT) final String cpuLimit,
                                                  @Value($KRAKEN_RUNTIME_MEMORY_REQUEST) final String memoryRequest,
                                                  @Value($KRAKEN_RUNTIME_MEMORY_LIMIT) final String memoryLimit,
                                                  @Value($KRAKEN_VERSION) final String version) {
    return RuntimeServerProperties.builder()
        .containersCount(ImmutableMap.of(TaskType.RUN, runContainersCount,
            TaskType.DEBUG, debugContainersCount,
            TaskType.RECORD, recordContainersCount))
        .version(version)
        .defaultAllocations(ResourcesAllocation.builder()
            .cpuRequest(cpuRequest)
            .cpuLimit(cpuLimit)
            .memoryRequest(memoryRequest)
            .memoryLimit(memoryLimit)
            .build())
        .build();
  }

}
