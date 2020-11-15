package com.octoperf.kraken.git.storage.events.listener;

import com.octoperf.kraken.git.event.GitStatusUpdateEvent;
import com.octoperf.kraken.storage.entity.StorageWatcherEvent;
import com.octoperf.kraken.storage.entity.StorageWatcherEventTest;
import com.octoperf.kraken.tools.event.bus.EventBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class FireGitStatusUpdateOnStorageEventTest {

  @Mock
  EventBus eventBus;

  FireGitStatusUpdateOnStorageEvent eventHandler;

  @Test
  void shouldFireEvent() {
    BDDMockito.given(eventBus.of(StorageWatcherEvent.class)).willReturn(Flux.empty());
    eventHandler = new FireGitStatusUpdateOnStorageEvent(eventBus);
    eventHandler.handleEvent(StorageWatcherEventTest.STORAGE_WATCHER_EVENT);
    Mockito.verify(eventBus).publish(GitStatusUpdateEvent.builder().owner(StorageWatcherEventTest.STORAGE_WATCHER_EVENT.getOwner()).build());
  }

}