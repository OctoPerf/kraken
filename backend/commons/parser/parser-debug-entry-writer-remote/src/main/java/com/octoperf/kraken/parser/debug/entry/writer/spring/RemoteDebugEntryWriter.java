package com.octoperf.kraken.parser.debug.entry.writer.spring;

import com.octoperf.kraken.analysis.client.api.AnalysisClient;
import com.octoperf.kraken.analysis.client.api.AnalysisClientBuilder;
import com.octoperf.kraken.analysis.entity.DebugEntry;
import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.parser.debug.entry.writer.api.DebugEntryWriter;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class RemoteDebugEntryWriter implements DebugEntryWriter {
  ContainerProperties containerProperties;
  Mono<AnalysisClient> clientMono;

  public RemoteDebugEntryWriter(@NonNull ContainerProperties containerProperties,
                                @NonNull AnalysisClientBuilder clientBuilder) {
    this.containerProperties = containerProperties;
    this.clientMono = clientBuilder.build(
        AuthenticatedClientBuildOrder.builder()
            .mode(AuthenticationMode.CONTAINER)
            .applicationId(containerProperties.getApplicationId())
            .projectId(containerProperties.getProjectId())
            .build()
    );
  }

  @Override
  public Flux<DebugEntry> write(final Flux<DebugEntry> entries) {
    return clientMono.flatMapMany(client -> entries
        .map(e -> e.withResultId(containerProperties.getTaskId()))
        .flatMap(client::debug));
  }
}
