package com.kraken.runtime.backend.docker;

import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.security.entity.owner.Owner;
import reactor.core.publisher.Mono;

public interface ContainerFindService {
  Mono<FlatContainer> find(Owner owner, String taskId, String containerName);
}
