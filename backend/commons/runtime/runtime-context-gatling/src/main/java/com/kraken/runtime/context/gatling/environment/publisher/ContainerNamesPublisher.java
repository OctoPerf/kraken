package com.kraken.runtime.context.gatling.environment.publisher;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.task.TaskType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.BACKEND;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ContainerNamesPublisher implements EnvironmentPublisher {

  private static final Map<TaskType, String> SIDEKICK_NAMES = ImmutableMap.of(TaskType.GATLING_RUN, "telegraf", TaskType.GATLING_DEBUG, "log-parser", TaskType.GATLING_RECORD, "har-parser");
  private static final Map<TaskType, String> SIDEKICK_LABELS = ImmutableMap.of(TaskType.GATLING_RUN, "Telegraf", TaskType.GATLING_DEBUG, "Log parser", TaskType.GATLING_RECORD, "HAR parser");

  @Override
  public boolean test(final TaskType taskType) {
    return true;
  }

  @Override
  public ExecutionContextBuilder apply(final ExecutionContextBuilder context) {
    return context.addEntries(ImmutableList.of(
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_GATLING_CONTAINER_NAME).value("gatling").build(),
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_GATLING_CONTAINER_LABEL).value("Gatling").build(),
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_GATLING_SIDEKICK_NAME).value(SIDEKICK_NAMES.get(context.getTaskType())).build(),
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_GATLING_SIDEKICK_LABEL).value(SIDEKICK_LABELS.get(context.getTaskType())).build()
    ));
  }
}
