package com.kraken.tools.event.bus;

import reactor.core.publisher.Flux;

public interface EventBus {
  void publish (BusEvent event);
  <T extends BusEvent> Flux<T> of(Class<T> eventClass);
}
