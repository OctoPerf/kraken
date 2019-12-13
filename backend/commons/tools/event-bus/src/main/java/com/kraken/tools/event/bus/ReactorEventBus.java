package com.kraken.tools.event.bus;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;

import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class ReactorEventBus implements EventBus {

  ReplayProcessor<BusEvent> rp = ReplayProcessor.create();

  @Override
  public void publish(BusEvent event) {
    try {
      rp.onNext(event);
    } catch (Throwable t) {
      log.error("Exception occurred during event handling " + event, t);
    }
  }

  @Override
  public <T extends BusEvent> Flux<T> of(Class<T> eventClass) {
    return rp
        .filter(busEvent -> busEvent.getClass().equals(eventClass))
        .map(eventClass::cast);
  }
}
