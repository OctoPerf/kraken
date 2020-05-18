package com.kraken.debug.entry.writer;

import com.kraken.analysis.client.api.AnalysisClient;
import com.kraken.analysis.client.api.AnalysisClientBuilder;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.config.runtime.container.api.ContainerProperties;
import com.kraken.security.authentication.api.AuthenticationMode;
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
final class RemoteDebugEntryWriter implements DebugEntryWriter {
  ContainerProperties containerProperties;
  AnalysisClient client;

  public RemoteDebugEntryWriter(@NonNull ContainerProperties containerProperties, @NonNull AnalysisClientBuilder clientBuilder){
    this.containerProperties = containerProperties;
    this.client = clientBuilder.mode(AuthenticationMode.CONTAINER).applicationId(containerProperties.getApplicationId()).build();
  }

  @Override
  public Flux<DebugEntry> write(final Flux<DebugEntry> entries) {
    return entries
      .map(e -> e.withResultId(containerProperties.getTaskId()))
      .flatMap(client::debug);
  }
}
