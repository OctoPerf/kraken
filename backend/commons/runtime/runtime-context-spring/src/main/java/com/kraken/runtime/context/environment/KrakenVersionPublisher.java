package com.kraken.runtime.context.environment;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.tools.properties.api.ApplicationProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.BACKEND;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_VERSION;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class KrakenVersionPublisher implements EnvironmentPublisher {

  @NonNull ApplicationProperties kraken;

  @Override
  public boolean test(final TaskType taskType) {
    return true;
  }

  @Override
  public ExecutionContextBuilder apply(final ExecutionContextBuilder context) {
    return context.addEntries(ImmutableList.of(
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_VERSION).value(kraken.getVersion()).build()
    ));
  }
}
