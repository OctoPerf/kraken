package com.octoperf.kraken.gatling.setup.api;

import reactor.core.publisher.Mono;

public interface GatlingSetupSimulationService {

  Mono<String> update(String content);

}
