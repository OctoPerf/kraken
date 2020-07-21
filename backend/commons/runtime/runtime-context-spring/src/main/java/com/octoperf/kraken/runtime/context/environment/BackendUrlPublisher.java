package com.octoperf.kraken.runtime.context.environment;

import com.octoperf.kraken.config.backend.client.api.BackendClientProperties;
import com.octoperf.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.octoperf.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.BACKEND;
import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class BackendUrlPublisher implements EnvironmentPublisher {

  @NonNull BackendClientProperties properties;

  @Override
  public boolean test(final TaskType taskType) {
    return true;
  }

  @Override
  public Mono<List<ExecutionEnvironmentEntry>> apply(final ExecutionContextBuilder context) {
    return Mono.just(of(
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_BACKEND_URL.name()).value(properties.getPublishedUrl()).build(),
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_HOSTNAME.name()).value(properties.getHostname()).build(),
        ExecutionEnvironmentEntry.builder().from(BACKEND).scope("").key(KRAKEN_IP.name()).value(properties.getIp()).build()
    ));
  }
}
