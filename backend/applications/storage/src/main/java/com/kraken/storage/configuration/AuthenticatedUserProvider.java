package com.kraken.storage.configuration;

import reactor.core.publisher.Mono;

public interface AuthenticatedUserProvider {
  Mono<String> getAuthenticatedUser();
}
