package com.octoperf.kraken.tools.sse.server;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.git.client.api.GitClientBuilder;
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
final class SSEGitChannelBuilder implements SSEChannelBuilder {

  @NonNull GitClientBuilder gitClientBuilder;

  @Override
  public SSEChannel get() {
    return SSEChannel.GIT;
  }

  @Override
  public Mono<Map<String, Flux<? extends Object>>> apply(final AuthenticatedClientBuildOrder order) {
    return gitClientBuilder.build(order)
        .map(gitClient -> ImmutableMap.of("GIT_STATUS", gitClient.watchStatus(),
            "GIT_REFRESH", gitClient.watchRefresh(),
            "GIT_LOG", gitClient.watchLogs()));
  }
}
