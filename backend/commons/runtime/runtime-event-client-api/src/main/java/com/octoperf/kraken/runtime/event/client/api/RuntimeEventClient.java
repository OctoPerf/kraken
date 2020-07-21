package com.octoperf.kraken.runtime.event.client.api;

import com.octoperf.kraken.runtime.event.TaskEvent;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClient;
import reactor.core.publisher.Flux;

public interface RuntimeEventClient extends AuthenticatedClient {
  Flux<TaskEvent> events();
}
