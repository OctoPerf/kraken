package com.kraken.commons.docker.client;

import com.kraken.commons.docker.entity.DockerImage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DockerImageClient {

  Flux<DockerImage> images();

  Mono<Boolean> rmi(String imageId);

}
