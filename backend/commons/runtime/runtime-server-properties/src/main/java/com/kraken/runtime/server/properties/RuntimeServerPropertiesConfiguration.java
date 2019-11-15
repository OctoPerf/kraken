package com.kraken.runtime.server.properties;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RuntimeServerPropertiesConfiguration {

  @Autowired
  @Bean
  RuntimeServerProperties runtimeServerProperties(@Value("${kraken.runtime.containers-count.run:#{environment.KRAKEN_RUNTIME_CONTAINERS_COUNT_RUN}}") final int runContainersCount,
                                           @Value("${kraken.runtime.containers-count.debug:#{environment.KRAKEN_RUNTIME_CONTAINERS_COUNT_DEBUG}}") final int debugContainersCount,
                                           @Value("${kraken.runtime.containers-count.record:#{environment.KRAKEN_RUNTIME_CONTAINERS_COUNT_RECORD}}") final int recordContainersCount,
                                           @Value("${kraken.version:#{environment.KRAKEN_VERSION}}") final String version) {
    return RuntimeServerProperties.builder()
        .containersCount(ImmutableMap.of(TaskType.RUN, runContainersCount,
            TaskType.DEBUG, debugContainersCount,
            TaskType.RECORD, recordContainersCount))
        .version(version)
        .build();
  }

}
