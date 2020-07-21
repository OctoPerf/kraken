package com.octoperf.kraken.runtime.backend.api;

import com.octoperf.kraken.runtime.entity.host.Host;
import com.octoperf.kraken.security.entity.owner.Owner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HostService {

  Flux<Host> list(Owner owner);

  Flux<Host> listAll();

  Mono<Host> detach(Host host);

  Mono<Host> attach(Host host);

}
