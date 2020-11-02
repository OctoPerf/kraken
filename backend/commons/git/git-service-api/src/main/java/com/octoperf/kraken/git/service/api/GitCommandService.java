package com.octoperf.kraken.git.service.api;

import com.octoperf.kraken.git.entity.GitStatus;
import com.octoperf.kraken.git.event.GitRefreshStorageEvent;
import com.octoperf.kraken.security.entity.owner.Owner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GitCommandService {

  Mono<Void> execute(Owner owner, String command);

  Mono<GitStatus> status(Owner owner);

  Flux<GitStatus> watchStatus(Owner owner);

  Flux<GitRefreshStorageEvent> watchRefresh(Owner owner);
}
