package com.kraken.runtime.docker.env;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.ExecutionContext;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.runtime.server.properties.RuntimeServerProperties;
import com.kraken.tools.environment.JavaOptsFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

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
    final var mapBuilder = ImmutableMap.<String, String>builder();
    mapBuilder.put(KRAKEN_TASK_ID, context.getTaskId());
    mapBuilder.put(KRAKEN_DESCRIPTION, context.getDescription());
    mapBuilder.put(KRAKEN_EXPECTED_COUNT, runtimeServerProperties.getContainersCount().get(context.getTaskType()).toString());
    mapBuilder.put(KRAKEN_APPLICATION_ID, context.getApplicationId());
    mapBuilder.put(KRAKEN_VERSION, runtimeServerProperties.getVersion());
    mapBuilder.put(KRAKEN_GATLING_JAVA_OPTS, javaOptsFactory.apply(context.getEnvironment()));
    return mapBuilder.build();
  }
}
