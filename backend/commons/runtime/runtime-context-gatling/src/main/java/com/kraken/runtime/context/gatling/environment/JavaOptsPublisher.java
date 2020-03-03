package com.kraken.runtime.context.gatling.environment;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.*;
import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.BACKEND;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class JavaOptsPublisher implements EnvironmentPublisher {

  @Override
  public boolean test(final String taskType) {
    return true;
  }

  @Override
  public ExecutionContextBuilder apply(final ExecutionContextBuilder context) {
    return context.addEntries(ImmutableList.of(
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_TASK_ID).value(context.getTaskId()).build(),
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_DESCRIPTION).value(context.getDescription()).build(),
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_APPLICATION_ID).value(context.getApplicationId()).build(),
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_TASK_TYPE).value(context.getTaskType()).build()
    ));

    // HostIds
//    final var mapBuilder = ImmutableMap.<String, String>builder();
//    mapBuilder.put(KRAKEN_TASK_ID, context.getTaskId());
//    mapBuilder.put(KRAKEN_DESCRIPTION, context.getDescription());
//    mapBuilder.put(KRAKEN_APPLICATION_ID, context.getApplicationId());
//    mapBuilder.put(KRAKEN_EXPECTED_COUNT, runtimeServerProperties.getContainersCount().get(context.getTaskType()).toString());
//    mapBuilder.put(KRAKEN_VERSION, runtimeServerProperties.getVersion());
//    TODO laisser l'env de départ ?
//    mapBuilder.put(KRAKEN_GATLING_JAVA_OPTS, javaOptsFactory.apply(context.getEnvironment()));
  }
}
