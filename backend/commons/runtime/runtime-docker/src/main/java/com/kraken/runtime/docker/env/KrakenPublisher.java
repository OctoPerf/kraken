package com.kraken.runtime.docker.env;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.ExecutionContext;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.server.properties.RuntimeServerProperties;
import com.kraken.tools.environment.JavaOptsFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_EXPECTED_COUNT;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class KrakenPublisher implements EnvironmentPublisher {

  @NonNull RuntimeServerProperties runtimeServerProperties;
  @NonNull JavaOptsFactory javaOptsFactory;

  @Override
  public boolean test(final TaskType taskType) {
    return true;
  }

  @Override
  public Map<String, String> apply(final ExecutionContext context) {
    return ImmutableMap.of(
        KRAKEN_TASK_ID, context.getTaskId(),
        KRAKEN_DESCRIPTION, context.getDescription(),
        KRAKEN_EXPECTED_COUNT, runtimeServerProperties.getContainersCount().get(context.getTaskType()).toString(),
        KRAKEN_VERSION, runtimeServerProperties.getVersion(),
        KRAKEN_GATLING_JAVA_OPTS, javaOptsFactory.apply(context.getEnvironment()));
  }
}
