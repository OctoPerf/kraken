package com.kraken.runtime.api;

import com.kraken.runtime.entity.Host;
import reactor.core.publisher.Flux;

public interface HostService {

  Flux<Host> list();

}
