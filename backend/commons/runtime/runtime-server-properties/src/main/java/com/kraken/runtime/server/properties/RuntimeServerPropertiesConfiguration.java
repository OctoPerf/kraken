package com.kraken.runtime.server.properties;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.ResourcesAllocation;
import com.kraken.runtime.entity.task.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.kraken.tools.environment.KrakenEnvironmentAtValues.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
@Configuration
public class RuntimeServerPropertiesConfiguration {

  @Autowired
  @Bean
  RuntimeServerProperties runtimeServerProperties(@Value($KRAKEN_RUNTIME_CONTAINERS_COUNT_RUN) final int runContainersCount,
                                                  @Value($KRAKEN_RUNTIME_CONTAINERS_COUNT_DEBUG) final int debugContainersCount,
                                                  @Value($KRAKEN_RUNTIME_CONTAINERS_COUNT_RECORD) final int recordContainersCount,
                                                  @Value($KRAKEN_RUNTIME_RUN_GATLING_CPU_REQUEST) final float runGatlingCpuRequest,
                                                  @Value($KRAKEN_RUNTIME_RUN_GATLING_CPU_LIMIT) final float runGatlingCpuLimit,
                                                  @Value($KRAKEN_RUNTIME_RUN_GATLING_MEMORY_REQUEST) final int runGatlingMemoryRequest,
                                                  @Value($KRAKEN_RUNTIME_RUN_GATLING_MEMORY_LIMIT) final int runGatlingMemoryLimit,
                                                  @Value($KRAKEN_RUNTIME_RUN_GATLING_MEMORY_PERCENTAGE) final float runGatlingMemoryPercentage,
                                                  @Value($KRAKEN_RUNTIME_RUN_TELEGRAF_CPU_REQUEST) final float runTelegrafCpuRequest,
                                                  @Value($KRAKEN_RUNTIME_RUN_TELEGRAF_CPU_LIMIT) final float runTelegrafCpuLimit,
                                                  @Value($KRAKEN_RUNTIME_RUN_TELEGRAF_MEMORY_REQUEST) final int runTelegrafMemoryRequest,
                                                  @Value($KRAKEN_RUNTIME_RUN_TELEGRAF_MEMORY_LIMIT) final int runTelegrafMemoryLimit,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_GATLING_CPU_REQUEST) final float debugGatlingCpuRequest,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_GATLING_CPU_LIMIT) final float debugGatlingCpuLimit,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_GATLING_MEMORY_REQUEST) final int debugGatlingMemoryRequest,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_GATLING_MEMORY_LIMIT) final int debugGatlingMemoryLimit,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_GATLING_MEMORY_PERCENTAGE) final float debugGatlingMemoryPercentage,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_LOG_PARSER_CPU_REQUEST) final float debugLogParserCpuRequest,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_LOG_PARSER_CPU_LIMIT) final float debugLogParserCpuLimit,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_LOG_PARSER_MEMORY_REQUEST) final int debugLogParserMemoryRequest,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_LOG_PARSER_MEMORY_LIMIT) final int debugLogParserMemoryLimit,
                                                  @Value($KRAKEN_RUNTIME_DEBUG_LOG_PARSER_MEMORY_PERCENTAGE) final float debugLogParserMemoryPercentage,
                                                  @Value($KRAKEN_RUNTIME_RECORD_GATLING_CPU_REQUEST) final float recordGatlingCpuRequest,
                                                  @Value($KRAKEN_RUNTIME_RECORD_GATLING_CPU_LIMIT) final float recordGatlingCpuLimit,
                                                  @Value($KRAKEN_RUNTIME_RECORD_GATLING_MEMORY_REQUEST) final int recordGatlingMemoryRequest,
                                                  @Value($KRAKEN_RUNTIME_RECORD_GATLING_MEMORY_LIMIT) final int recordGatlingMemoryLimit,
                                                  @Value($KRAKEN_RUNTIME_RECORD_GATLING_MEMORY_PERCENTAGE) final float recordGatlingMemoryPercentage,
                                                  @Value($KRAKEN_RUNTIME_RECORD_HAR_PARSER_CPU_REQUEST) final float recordHarParserCpuRequest,
                                                  @Value($KRAKEN_RUNTIME_RECORD_HAR_PARSER_CPU_LIMIT) final float recordHarParserCpuLimit,
                                                  @Value($KRAKEN_RUNTIME_RECORD_HAR_PARSER_MEMORY_REQUEST) final int recordHarParserMemoryRequest,
                                                  @Value($KRAKEN_RUNTIME_RECORD_HAR_PARSER_MEMORY_LIMIT) final int recordHarParserMemoryLimit,
                                                  @Value($KRAKEN_RUNTIME_RECORD_HAR_PARSER_MEMORY_PERCENTAGE) final float recordHarParserMemoryPercentage,
                                                  @Value($KRAKEN_VERSION) final String version) {

    return RuntimeServerProperties.builder()
        .containersCount(ImmutableMap.of(TaskType.RUN, runContainersCount,
            TaskType.DEBUG, debugContainersCount,
            TaskType.RECORD, recordContainersCount))
        .version(version)
        .defaultAllocations(
            ImmutableMap.of(
                TaskType.RUN, ImmutableMap.of("gatling", ResourcesAllocation.builder().cpuRequest(runGatlingCpuRequest).cpuLimit(runGatlingCpuLimit).memoryRequest(runGatlingMemoryRequest).memoryLimit(runGatlingMemoryLimit).memoryPercentage(of(runGatlingMemoryPercentage)).build(),
                    "telegraf", ResourcesAllocation.builder().cpuRequest(runTelegrafCpuRequest).cpuLimit(runTelegrafCpuLimit).memoryRequest(runTelegrafMemoryRequest).memoryLimit(runTelegrafMemoryLimit).memoryPercentage(empty()).build()),
                TaskType.DEBUG, ImmutableMap.of("gatling", ResourcesAllocation.builder().cpuRequest(debugGatlingCpuRequest).cpuLimit(debugGatlingCpuLimit).memoryRequest(debugGatlingMemoryRequest).memoryLimit(debugGatlingMemoryLimit).memoryPercentage(of(debugGatlingMemoryPercentage)).build(),
                    "log-parser", ResourcesAllocation.builder().cpuRequest(debugLogParserCpuRequest).cpuLimit(debugLogParserCpuLimit).memoryRequest(debugLogParserMemoryRequest).memoryLimit(debugLogParserMemoryLimit).memoryPercentage(of(debugLogParserMemoryPercentage)).build()),
                TaskType.RECORD, ImmutableMap.of("gatling", ResourcesAllocation.builder().cpuRequest(recordGatlingCpuRequest).cpuLimit(recordGatlingCpuLimit).memoryRequest(recordGatlingMemoryRequest).memoryLimit(recordGatlingMemoryLimit).memoryPercentage(of(recordGatlingMemoryPercentage)).build(),
                    "har-parser", ResourcesAllocation.builder().cpuRequest(recordHarParserCpuRequest).cpuLimit(recordHarParserCpuLimit).memoryRequest(recordHarParserMemoryRequest).memoryLimit(recordHarParserMemoryLimit).memoryPercentage(of(recordHarParserMemoryPercentage)).build())
            )
        )
        .build();
  }

}
