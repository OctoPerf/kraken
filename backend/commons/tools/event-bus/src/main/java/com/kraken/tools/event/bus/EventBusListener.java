package com.kraken.tools.event.bus;

import java.util.Objects;

public abstract class EventBusListener<T extends BusEvent> {

  public EventBusListener(final EventBus eventBus, final Class<T> clazz) {
    Objects.requireNonNull(eventBus);
    Objects.requireNonNull(clazz);
    eventBus.of(clazz).subscribe(this::handleEvent);
  }

  protected abstract void handleEvent(T event);
}
