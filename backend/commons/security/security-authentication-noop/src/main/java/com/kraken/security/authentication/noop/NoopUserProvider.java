package com.kraken.security.authentication.noop;

import com.kraken.security.authentication.api.UserProvider;
import com.kraken.security.entity.token.KrakenTokenUser;
import reactor.core.publisher.Mono;

class NoopUserProvider implements UserProvider {
  @Override
  public Mono<KrakenTokenUser> getAuthenticatedUser() {
    return Mono.error(new IllegalAccessException("Noop authentication does not provide a user."));
  }

  @Override
  public Mono<String> getTokenValue() {
    return Mono.error(new IllegalAccessException("Noop authentication does not provide a token value."));
  }

}
