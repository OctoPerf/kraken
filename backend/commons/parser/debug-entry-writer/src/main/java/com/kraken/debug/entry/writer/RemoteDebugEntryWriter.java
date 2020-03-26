package com.kraken.debug.entry.writer;

import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class RemoteDebugEntryWriter implements DebugEntryWriter {
  @NonNull RuntimeContainerProperties container;
  @NonNull AnalysisClient client;

  @Override
  public Flux<DebugEntry> write(final Flux<DebugEntry> entries) {
    return entries
      .map(e -> e.withResultId(container.getTaskId()))
      .flatMap(client::debug);
  }
}
