package com.octoperf.kraken.runtime.context.environment;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.config.api.ApplicationProperties;
import com.octoperf.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.octoperf.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.BACKEND;
import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_VERSION;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class KrakenVersionPublisher implements EnvironmentPublisher {

  @NonNull ApplicationProperties kraken;

  @Override
  public boolean test(final TaskType taskType) {
    return true;
  }

  @Override
  public Mono<List<ExecutionEnvironmentEntry>> apply(final ExecutionContextBuilder context) {
    return Mono.just(ImmutableList.of(
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_VERSION.name()).value(kraken.getVersion()).build()
    ));
  }
}
