package com.kraken.runtime.event.client.api;

import com.kraken.runtime.event.TaskEvent;
import com.kraken.security.authentication.client.api.AuthenticatedClient;
import reactor.core.publisher.Flux;

public interface RuntimeEventClient extends AuthenticatedClient {
  Flux<TaskEvent> events();
}
