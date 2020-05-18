package com.kraken.tools.event.bus;

import lombok.NonNull;

public abstract class EventBusListener<T extends BusEvent> {

  public EventBusListener(@NonNull final EventBus eventBus,
                          @NonNull final Class<T> clazz) {
    eventBus.of(clazz).subscribe(this::handleEvent);
  }

  protected abstract void handleEvent(T event);
}
