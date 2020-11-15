package com.octoperf.kraken.tools.sse.server;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.runtime.client.api.RuntimeClientBuilder;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
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
final class SSERuntimeChannelBuilder implements SSEChannelBuilder {

  @NonNull RuntimeClientBuilder runtimeClientBuilder;

  @Override
  public SSEChannel get() {
    return SSEChannel.RUNTIME;
  }

  @Override
  public Mono<Map<String, Flux<? extends Object>>> apply(final AuthenticatedClientBuildOrder order) {
    return runtimeClientBuilder.build(order)
        .map(runtimeClient -> ImmutableMap.of("LOG", runtimeClient.watchLogs(),
            "TASKS", runtimeClient.watchTasks()));
  }
}