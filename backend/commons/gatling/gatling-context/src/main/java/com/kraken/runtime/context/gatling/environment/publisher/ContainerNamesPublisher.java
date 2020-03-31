package com.kraken.runtime.context.gatling.environment.publisher;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.task.TaskType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry.builder;
import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.BACKEND;
import static com.kraken.runtime.entity.task.TaskType.*;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ContainerNamesPublisher implements EnvironmentPublisher {

  private static final Map<TaskType, String> SIDEKICK_NAMES = of(
    GATLING_RUN, "telegraf",
    GATLING_DEBUG, "log-parser",
    GATLING_RECORD, "har-parser"
  );
  private static final Map<TaskType, String> SIDEKICK_LABELS = of(
    GATLING_RUN, "Telegraf",
    GATLING_DEBUG, "Log parser",
    GATLING_RECORD, "HAR parser"
  );

  @Override
  public boolean test(final TaskType taskType) {
    return true;
  }

  @Override
  public ExecutionContextBuilder apply(final ExecutionContextBuilder context) {
    return context.addEntries(ImmutableList.of(
      builder().from(BACKEND).scope("").key(KRAKEN_GATLING_CONTAINER_NAME.name()).value("gatling").build(),
      builder().from(BACKEND).scope("").key(KRAKEN_GATLING_CONTAINER_LABEL.name()).value("Gatling").build(),
      builder().from(BACKEND).scope("").key(KRAKEN_GATLING_SIDEKICK_NAME.name()).value(SIDEKICK_NAMES.get(context.getTaskType())).build(),
      builder().from(BACKEND).scope("").key(KRAKEN_GATLING_SIDEKICK_LABEL.name()).value(SIDEKICK_LABELS.get(context.getTaskType())).build()
    ));
  }
}
