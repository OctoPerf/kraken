package com.octoperf.kraken.tools.sse.server;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.storage.client.api.StorageClientBuilder;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class SSEStorageChannelBuilder implements SSEChannelBuilder {

  @NonNull StorageClientBuilder storageClientBuilder;

  @Override
  public SSEChannel get() {
    return SSEChannel.STORAGE;
  }

  @Override
  public Mono<Map<String, Flux<? extends Object>>> apply(final AuthenticatedClientBuildOrder order) {
    return storageClientBuilder.build(order)
        .map(storageClient -> ImmutableMap.of("NODE", storageClient.watch()));
  }
}
