package com.kraken.security.authentication.container;

import com.google.common.annotations.VisibleForTesting;
import com.kraken.config.security.client.api.SecurityClientProperties;
import com.kraken.config.security.container.api.SecurityContainerProperties;
import com.kraken.security.authentication.utils.AtomicUserProvider;
import com.kraken.security.client.api.SecurityClient;
import com.kraken.security.decoder.api.TokenDecoder;
import com.kraken.security.entity.token.KrakenToken;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ContainerUserProvider extends AtomicUserProvider {

  SecurityClientProperties clientProperties;
  SecurityContainerProperties containerProperties;
  SecurityClient client;

  public ContainerUserProvider(@NonNull final SecurityClientProperties clientProperties,
                               @NonNull final SecurityContainerProperties containerProperties,
                               @NonNull final TokenDecoder decoder,
                               @NonNull final SecurityClient client) {
    super(decoder, containerProperties.getMinValidity());
    this.clientProperties = clientProperties;
    this.containerProperties = containerProperties;
    this.client = client;
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
    return client.refreshToken(clientProperties.getContainer(), token.getRefreshToken());
  }

}
