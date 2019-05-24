package com.kraken.commons.docker.client.compose;

import reactor.core.publisher.Mono;

import java.util.Optional;

public interface DockerComposeService {

  Mono<String> up(String applicationId, String path);

  Mono<String> down(String applicationId, String path);

  Mono<String> ps(String applicationId, String path);

  Mono<String> logs(String applicationId, String path);
}
