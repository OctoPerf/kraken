package com.kraken.runtime.server.service;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface RuntimeService {

  Mono<String> run(String applicationId,
                   String description,
                   Map<String, String> environment);

  Mono<String> debug(String applicationId,
                     String description,
                     Map<String, String> environment);

  Mono<String> record(String applicationId,
                      Map<String, String> environment);
}
