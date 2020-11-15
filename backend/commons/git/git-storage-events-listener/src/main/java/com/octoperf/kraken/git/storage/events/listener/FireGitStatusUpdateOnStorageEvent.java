package com.octoperf.kraken.git.storage.events.listener;

import com.octoperf.kraken.git.event.GitStatusUpdateEvent;
import com.octoperf.kraken.storage.entity.StorageWatcherEvent;
import com.octoperf.kraken.tools.event.bus.EventBus;
import com.octoperf.kraken.tools.event.bus.EventBusListener;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class FireGitStatusUpdateOnStorageEvent extends EventBusListener<StorageWatcherEvent> {

  EventBus eventBus;

  @Autowired
  FireGitStatusUpdateOnStorageEvent(final EventBus eventBus) {
    super(eventBus, StorageWatcherEvent.class);
    this.eventBus = eventBus;
  }

  @Override
  protected void handleEvent(final StorageWatcherEvent event) {
    eventBus.publish(GitStatusUpdateEvent.builder().owner(event.getOwner()).build());
  }
}
