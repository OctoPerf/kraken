package com.kraken.commons.docker.client.spotify;

import com.kraken.commons.docker.client.entity.DockerContainer;
import com.kraken.commons.docker.client.entity.DockerImage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DockerService {

  Mono<String> pull(String applicationId, String image);

  Mono<String> run(String name, String config);

  Mono<Boolean> start(String containerId);

  Mono<Boolean> stop(String containerId, int secondsToWaitBeforeKilling);

  Mono<String> logs(String applicationId, String containerId);

  Mono<String> tail(String containerId, Integer lines);

  Flux<DockerContainer> ps();

  Mono<DockerContainer> inspect(String containerId);

  Mono<Boolean> rm(String containerId);

  Flux<DockerImage> images();

  Mono<Boolean> rmi(String imageId);

  Mono<String> prune(String applicationId, boolean all, boolean volumes);
}
