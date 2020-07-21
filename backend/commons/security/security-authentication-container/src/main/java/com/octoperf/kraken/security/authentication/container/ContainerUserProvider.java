package com.octoperf.kraken.security.authentication.container;

import com.google.common.annotations.VisibleForTesting;
import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
import com.octoperf.kraken.config.security.container.api.SecurityContainerProperties;
import com.octoperf.kraken.security.authentication.utils.AtomicUserProvider;
import com.octoperf.kraken.security.client.api.SecurityClient;
import com.octoperf.kraken.security.decoder.api.TokenDecoder;
import com.octoperf.kraken.security.entity.token.KrakenToken;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Function;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ContainerUserProvider extends AtomicUserProvider {

  SecurityClientProperties clientProperties;
  SecurityContainerProperties containerProperties;
  Mono<SecurityClient> clientMono;

  public ContainerUserProvider(@NonNull final SecurityClientProperties clientProperties,
                               @NonNull final SecurityContainerProperties containerProperties,
                               @NonNull final TokenDecoder decoder,
                               @NonNull final Mono<SecurityClient> clientMono) {
    super(decoder, containerProperties.getMinValidity());
    this.clientProperties = clientProperties;
    this.containerProperties = containerProperties;
    this.clientMono = clientMono;
    this.periodicRefresh(aLong -> super.getTokenValue());
  }


  @VisibleForTesting
  Flux<String> periodicRefresh(final Function<Long, Mono<String>> refresh) {
    final var duration = Duration.ofSeconds(containerProperties.getRefreshExpiresIn())
        .minusSeconds(containerProperties.getRefreshMinValidity());

    return Flux.interval(Duration.ZERO, duration)
        .flatMap(refresh);
  }

  @Override
  protected Mono<KrakenToken> newToken() {
    return Mono.just(KrakenToken.builder()
        .accessToken(containerProperties.getAccessToken())
        .refreshToken(containerProperties.getRefreshToken())
        .expiresIn(containerProperties.getExpiresIn())
        .refreshExpiresIn(containerProperties.getRefreshExpiresIn())
        .build()).flatMap(this::refreshToken);
  }

  @Override
  protected Mono<KrakenToken> refreshToken(KrakenToken token) {
    return clientMono.flatMap(client -> client.refreshToken(clientProperties.getContainer(), token.getRefreshToken()));
  }

}
