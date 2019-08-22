package com.kraken.commons.docker.spotify;

import com.kraken.commons.docker.entity.DockerContainer;
import com.kraken.commons.docker.entity.DockerImage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DockerService {

  Mono<String> run(String name, String config);

  Mono<Boolean> start(String containerId);

  Mono<Boolean> stop(String containerId, int secondsToWaitBeforeKilling);

  Mono<String> tail(String containerId, Integer lines);

  Flux<DockerContainer> ps();

  Mono<DockerContainer> inspect(String containerId);

  Mono<Boolean> rm(String containerId);

  Flux<DockerImage> images();

  Mono<Boolean> rmi(String imageId);

}
