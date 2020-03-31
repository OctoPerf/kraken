package com.kraken.security.configuration;

import com.kraken.security.configuration.entity.KrakenUser;
import reactor.core.publisher.Mono;

public interface AuthenticatedUserProvider {
  Mono<KrakenUser> getAuthenticatedUser();
  Mono<String> getAuthorizationHeader();
}
