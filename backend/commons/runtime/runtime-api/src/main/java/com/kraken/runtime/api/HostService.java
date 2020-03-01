package com.kraken.runtime.api;

import com.kraken.runtime.entity.host.Host;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HostService {

  Flux<Host> list();

  Flux<Host> listAll();

  Mono<Host> detach(Host host);

  Mono<Host> attach(Host host);

}
