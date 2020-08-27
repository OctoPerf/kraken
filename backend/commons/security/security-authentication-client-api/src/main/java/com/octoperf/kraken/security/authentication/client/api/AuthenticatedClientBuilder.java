package com.octoperf.kraken.security.authentication.client.api;

import reactor.core.publisher.Mono;

public interface AuthenticatedClientBuilder<T extends AuthenticatedClient> {

  Mono<T> build(AuthenticatedClientBuildOrder order);
}
