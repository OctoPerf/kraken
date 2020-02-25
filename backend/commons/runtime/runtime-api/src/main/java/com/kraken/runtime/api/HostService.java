package com.kraken.runtime.api;

import com.kraken.runtime.entity.Host;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface HostService {

  Flux<Host> list();

  Flux<Host> listAll();

  Mono<Void> detach(Host host);

  Mono<Host> attach(Host host);

}
