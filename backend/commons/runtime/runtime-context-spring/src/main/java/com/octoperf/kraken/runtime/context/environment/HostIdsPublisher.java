package com.octoperf.kraken.runtime.context.environment;

import com.octoperf.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.octoperf.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.BACKEND;
import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_HOST_ID;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class HostIdsPublisher implements EnvironmentPublisher {

  @Override
  public boolean test(final TaskType taskType) {
    return true;
  }

  @Override
  public Mono<List<ExecutionEnvironmentEntry>> apply(final ExecutionContextBuilder context) {
    return Mono.just(context.getHostIds().stream().map(hostId -> ExecutionEnvironmentEntry.builder().from(BACKEND).scope(hostId).key(KRAKEN_HOST_ID.name()).value(hostId).build()).collect(Collectors.toUnmodifiableList()));
  }
}
