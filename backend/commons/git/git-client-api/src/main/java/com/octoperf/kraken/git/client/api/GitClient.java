package com.octoperf.kraken.git.client.api;

import com.octoperf.kraken.git.entity.GitConfiguration;
import com.octoperf.kraken.git.entity.GitLog;
import com.octoperf.kraken.git.entity.GitStatus;
import com.octoperf.kraken.git.event.GitRefreshStorageEvent;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GitClient extends AuthenticatedClient {

  Mono<GitConfiguration> connect(String repositoryUrl);

  Flux<GitLog> watchLogs();

  Flux<GitStatus> watchStatus();

  Flux<GitRefreshStorageEvent> watchRefresh();
}
