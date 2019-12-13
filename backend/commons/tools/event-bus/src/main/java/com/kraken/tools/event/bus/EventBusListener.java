package com.kraken.tools.event.bus;

public abstract class EventBusListener<T extends BusEvent> {

  EventBusListener(final EventBus eventBus, final Class<T> clazz) {
    eventBus.of(clazz).subscribe(this::handleEvent);
  }

  protected abstract void handleEvent(T event);
}
