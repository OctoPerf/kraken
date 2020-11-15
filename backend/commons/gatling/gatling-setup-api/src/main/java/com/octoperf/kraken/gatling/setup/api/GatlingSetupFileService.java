package com.octoperf.kraken.gatling.setup.api;

import reactor.core.publisher.Mono;

public interface GatlingSetupFileService {

  Mono<String> loadSimulation();

  Mono<Void> saveSimulation(String content);

}
