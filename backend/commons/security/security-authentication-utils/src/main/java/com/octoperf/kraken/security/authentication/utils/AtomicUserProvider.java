package com.octoperf.kraken.security.authentication.utils;

import com.octoperf.kraken.security.authentication.api.UserProvider;
import com.octoperf.kraken.security.decoder.api.TokenDecoder;
import com.octoperf.kraken.security.entity.token.KrakenToken;
import com.octoperf.kraken.security.entity.token.KrakenTokenUser;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.empty;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AtomicUserProvider implements UserProvider {

  TokenDecoder decoder;
  AtomicReference<Optional<KrakenToken>> token;
  Long minValidity;

  public AtomicUserProvider(@NonNull final TokenDecoder decoder,
                            @NonNull final Long minValidity) {
    this.decoder = decoder;
    this.minValidity = minValidity;
    this.token = new AtomicReference<>(empty());
  }

  @Override
  public Mono<KrakenTokenUser> getAuthenticatedUser() {
    return this.getTokenValue().flatMap(currentToken -> Mono.fromCallable(() -> decoder.decode(currentToken)));
  }

  @Override
  public Mono<String> getTokenValue() {
    return Mono.just(this.token.get())
        .flatMap(currentToken -> {
          if (currentToken.isEmpty()) {
            return this.newToken().doOnNext(this::setToken);
          } else {
            return Mono
                .fromCallable(() -> decoder.decode(currentToken.get().getAccessToken()))
                .flatMap(user -> {
                  if (user.getExpirationTime().minusSeconds(minValidity).isBefore(Instant.now())) {
                    return refreshToken(currentToken.get()).doOnNext(this::setToken);
                  }
                  return Mono.just(currentToken.get());
                });
          }
        }).map(KrakenToken::getAccessToken);
  }

  protected abstract Mono<KrakenToken> newToken();

  protected abstract Mono<KrakenToken> refreshToken(KrakenToken token);

  private void setToken(final KrakenToken token) {
    this.token.set(Optional.of(token));
  }
}
