package com.octoperf.kraken.runtime.backend.docker;

import com.octoperf.kraken.runtime.entity.task.FlatContainer;
import com.octoperf.kraken.security.entity.owner.Owner;
import reactor.core.publisher.Mono;

public interface ContainerFindService {
  Mono<FlatContainer> find(Owner owner, String taskId, String containerName);
}
