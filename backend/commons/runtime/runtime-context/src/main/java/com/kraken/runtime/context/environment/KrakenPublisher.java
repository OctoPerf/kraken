package com.kraken.runtime.context.environment;

import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.server.properties.RuntimeServerProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class KrakenPublisher implements EnvironmentPublisher {

  @NonNull RuntimeServerProperties runtimeServerProperties;

  @Override
  public boolean test(final String taskType) {
    return true;
  }

  @Override
  public ExecutionContextBuilder apply(final ExecutionContextBuilder context) {
//    final var mapBuilder = ImmutableMap.<String, String>builder();
//    mapBuilder.put(KRAKEN_TASK_ID, context.getTaskId());
//    mapBuilder.put(KRAKEN_DESCRIPTION, context.getDescription());
//    mapBuilder.put(KRAKEN_APPLICATION_ID, context.getApplicationId());
//    mapBuilder.put(KRAKEN_EXPECTED_COUNT, runtimeServerProperties.getContainersCount().get(context.getTaskType()).toString());
//    mapBuilder.put(KRAKEN_VERSION, runtimeServerProperties.getVersion());
//    TODO laisser l'env de départ ?
//    mapBuilder.put(KRAKEN_GATLING_JAVA_OPTS, javaOptsFactory.apply(context.getEnvironment()));
    return context;
  }
}
