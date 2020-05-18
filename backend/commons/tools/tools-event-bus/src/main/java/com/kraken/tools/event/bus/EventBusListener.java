package com.kraken.tools.event.bus;

import static java.util.Objects.requireNonNull;

public abstract class EventBusListener<T extends BusEvent> {

  public EventBusListener(final EventBus eventBus, final Class<T> clazz) {
    requireNonNull(eventBus);
    requireNonNull(clazz);
    eventBus.of(clazz).subscribe(this::handleEvent);
  }

  protected abstract void handleEvent(T event);
}
