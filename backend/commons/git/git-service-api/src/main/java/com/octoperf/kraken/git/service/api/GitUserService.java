package com.octoperf.kraken.git.service.api;

import com.octoperf.kraken.git.entity.GitCredentials;
import reactor.core.publisher.Mono;

public interface GitUserService {

  Mono<GitCredentials> getCredentials(String userId);

  Mono<GitCredentials> initCredentials(String userId);

  Mono<Void> removeCredentials(String userId);
}
