package com.kraken.docker.client;

import com.kraken.docker.entity.DockerImage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DockerImageClient {

  Flux<DockerImage> images();

  Mono<Boolean> rmi(String imageId);

}
