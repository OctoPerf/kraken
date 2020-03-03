package com.kraken.runtime.context.environment;

import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.BACKEND;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_HOST_ID;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class HostIdsPublisher implements EnvironmentPublisher {

  @Override
  public boolean test(final String taskType) {
    return true;
  }

  @Override
  public ExecutionContextBuilder apply(final ExecutionContextBuilder context) {
    return context.addEntries(context.getHostIds().stream().map(hostId -> ExecutionEnvironmentEntry.builder().from(BACKEND).scope(hostId).key(KRAKEN_HOST_ID).value(hostId).build()).collect(Collectors.toUnmodifiableList()));
  }
}
