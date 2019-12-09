package com.kraken.debug.entry.writer;

import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static java.util.Objects.requireNonNull;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
class RemoteDebugEntryWriter implements DebugEntryWriter {

  @NonNull RuntimeContainerProperties containerProperties;
  @NonNull AnalysisClient client;

  @Override
  public Flux<DebugEntry> write(final Flux<DebugEntry> entries) {
    return entries.map(entry -> entry.withResultId(this.containerProperties.getTaskId()))
        .flatMap(client::debug);
  }
}
