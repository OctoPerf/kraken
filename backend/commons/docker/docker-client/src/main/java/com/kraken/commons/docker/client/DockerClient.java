package com.kraken.commons.docker.client;

import com.kraken.commons.docker.entity.DockerContainer;
import com.kraken.commons.docker.entity.DockerImage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface DockerClient {

  Mono<String> run(String name, String config);

  Mono<Boolean> start(String containerId);

  Mono<Boolean> stop(String containerId, Optional<Integer> secondsToWaitBeforeKilling);

  Mono<String> tail(String containerId, Optional<Integer> lines);

  Flux<DockerContainer> ps();

  Mono<DockerContainer> inspect(String containerId);

  Mono<Boolean> rm(String containerId);

  Flux<DockerImage> images();

  Mono<Boolean> rmi(String imageId);

}
