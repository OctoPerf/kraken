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
                                                  @Value($KRAKEN_RUNTIME_RUN_GATLING_CPU_REQUEST) final String runGatlingCpuRequest,
                                                  @Value($KRAKEN_RUNTIME_RUN_GATLING_CPU_LIMIT) final String runGatlingCpuLimit,
                                                  @Value($KRAKEN_RUNTIME_RUN_GATLING_MEMORY_REQUEST) final String runGatlingMemoryRequest,
                                                  @Value($KRAKEN_RUNTIME_RUN_GATLING_MEMORY_LIMIT) final String runGatlingMemoryLimit,
                                                  @Value($KRAKEN_RUNTIME_RUN_TELEGRAF_CPU_REQUEST) final String runTelegrafCpuRequest,
                                                  @Value($KRAKEN_RUNTIME_RUN_TELEGRAF_CPU_LIMIT) final String runTelegrafCpuLimit,
                                                  @Value($KRAKEN_RUNTIME_RUN_TELEGRAF_MEMORY_REQUEST) final String runTelegrafMemoryRequest,
                                                  @Value($KRAKEN_RUNTIME_RUN_TELEGRAF_MEMORY_LIMIT) final String runTelegrafMemoryLimit,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_GATLING_CPU_REQUEST) final String debugGatlingCpuRequest,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_GATLING_CPU_LIMIT) final String debugGatlingCpuLimit,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_GATLING_MEMORY_REQUEST) final String debugGatlingMemoryRequest,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_GATLING_MEMORY_LIMIT) final String debugGatlingMemoryLimit,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_LOG_PARSER_CPU_REQUEST) final String debugLogParserCpuRequest,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_LOG_PARSER_CPU_LIMIT) final String debugLogParserCpuLimit,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_LOG_PARSER_MEMORY_REQUEST) final String debugLogParserMemoryRequest,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_LOG_PARSER_MEMORY_LIMIT) final String debugLogParserMemoryLimit,
                                                  @Value($KRAKEN_RUNTIME_RECORD_GATLING_CPU_REQUEST) final String recordGatlingCpuRequest,
                                                  @Value($KRAKEN_RUNTIME_RECORD_GATLING_CPU_LIMIT) final String recordGatlingCpuLimit,
                                                  @Value($KRAKEN_RUNTIME_RECORD_GATLING_MEMORY_REQUEST) final String recordGatlingMemoryRequest,
                                                  @Value($KRAKEN_RUNTIME_RECORD_GATLING_MEMORY_LIMIT) final String recordGatlingMemoryLimit,
                                                  @Value($KRAKEN_RUNTIME_RECORD_HAR_PARSER_CPU_REQUEST) final String recordHarParserCpuRequest,
                                                  @Value($KRAKEN_RUNTIME_RECORD_HAR_PARSER_CPU_LIMIT) final String recordHarParserCpuLimit,
                                                  @Value($KRAKEN_RUNTIME_RECORD_HAR_PARSER_MEMORY_REQUEST) final String recordHarParserMemoryRequest,
                                                  @Value($KRAKEN_RUNTIME_RECORD_HAR_PARSER_MEMORY_LIMIT) final String recordHarParserMemoryLimit,
                                                  @Value($KRAKEN_VERSION) final String version) {

    return RuntimeServerProperties.builder()
        .containersCount(ImmutableMap.of(TaskType.RUN, runContainersCount,
            TaskType.DEBUG, debugContainersCount,
            TaskType.RECORD, recordContainersCount))
        .version(version)
        .defaultAllocations(
            ImmutableMap.of(
                TaskType.RUN, ImmutableMap.of("gatling", ResourcesAllocation.builder().cpuRequest(runGatlingCpuRequest).cpuLimit(runGatlingCpuLimit).memoryRequest(runGatlingMemoryRequest).memoryLimit(runGatlingMemoryLimit).build(),
                    "telegraf", ResourcesAllocation.builder().cpuRequest(runTelegrafCpuRequest).cpuLimit(runTelegrafCpuLimit).memoryRequest(runTelegrafMemoryRequest).memoryLimit(runTelegrafMemoryLimit).build()),
                TaskType.DEBUG, ImmutableMap.of("gatling", ResourcesAllocation.builder().cpuRequest(debugGatlingCpuRequest).cpuLimit(debugGatlingCpuLimit).memoryRequest(debugGatlingMemoryRequest).memoryLimit(debugGatlingMemoryLimit).build(),
                    "log-parser", ResourcesAllocation.builder().cpuRequest(debugLogParserCpuRequest).cpuLimit(debugLogParserCpuLimit).memoryRequest(debugLogParserMemoryRequest).memoryLimit(debugLogParserMemoryLimit).build()),
                TaskType.RECORD, ImmutableMap.of("gatling", ResourcesAllocation.builder().cpuRequest(recordGatlingCpuRequest).cpuLimit(recordGatlingCpuLimit).memoryRequest(recordGatlingMemoryRequest).memoryLimit(recordGatlingMemoryLimit).build(),
                    "har-parser", ResourcesAllocation.builder().cpuRequest(recordHarParserCpuRequest).cpuLimit(recordHarParserCpuLimit).memoryRequest(recordHarParserMemoryRequest).memoryLimit(recordHarParserMemoryLimit).build())
            )
        )
        .build();
  }

}
