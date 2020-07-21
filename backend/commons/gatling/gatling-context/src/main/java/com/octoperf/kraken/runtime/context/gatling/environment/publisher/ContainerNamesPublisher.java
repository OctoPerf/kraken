package com.octoperf.kraken.runtime.context.gatling.environment.publisher;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.octoperf.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class ContainerNamesPublisher implements EnvironmentPublisher {

  private static final Map<TaskType, String> SIDEKICK_NAMES = of(
    TaskType.GATLING_RUN, "telegraf",
    TaskType.GATLING_DEBUG, "log-parser",
    TaskType.GATLING_RECORD, "har-parser"
  );
  private static final Map<TaskType, String> SIDEKICK_LABELS = of(
    TaskType.GATLING_RUN, "Telegraf",
    TaskType.GATLING_DEBUG, "Log parser",
    TaskType.GATLING_RECORD, "HAR parser"
  );

  @Override
  public boolean test(final TaskType taskType) {
    return true;
  }

  @Override
  public Mono<List<ExecutionEnvironmentEntry>> apply(final ExecutionContextBuilder context) {
    return Mono.just(ImmutableList.of(
      ExecutionEnvironmentEntry.builder().from(ExecutionEnvironmentEntrySource.BACKEND).scope("").key(KRAKEN_GATLING_CONTAINER_NAME.name()).value("gatling").build(),
      ExecutionEnvironmentEntry.builder().from(ExecutionEnvironmentEntrySource.BACKEND).scope("").key(KRAKEN_GATLING_CONTAINER_LABEL.name()).value("Gatling").build(),
      ExecutionEnvironmentEntry.builder().from(ExecutionEnvironmentEntrySource.BACKEND).scope("").key(KRAKEN_GATLING_SIDEKICK_NAME.name()).value(SIDEKICK_NAMES.get(context.getTaskType())).build(),
      ExecutionEnvironmentEntry.builder().from(ExecutionEnvironmentEntrySource.BACKEND).scope("").key(KRAKEN_GATLING_SIDEKICK_LABEL.name()).value(SIDEKICK_LABELS.get(context.getTaskType())).build()
    ));
  }
}
