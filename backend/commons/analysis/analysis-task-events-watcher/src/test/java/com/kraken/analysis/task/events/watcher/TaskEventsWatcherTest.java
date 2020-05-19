package com.kraken.analysis.task.events.watcher;

import com.kraken.runtime.event.TaskCreatedEventTest;
import com.kraken.runtime.event.client.api.RuntimeEventClient;
import com.kraken.runtime.event.client.api.RuntimeEventClientBuilder;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.tools.event.bus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TaskEventsWatcherTest {

  @Mock
  RuntimeEventClientBuilder clientBuilder;

  @Mock
  RuntimeEventClient client;
  @Mock
  EventBus eventBus;

  TaskEventsWatcher watcher;

  @BeforeEach
  public void setUp() {
    watcher = new TaskEventsWatcher(clientBuilder, eventBus);
  }

  @Test
  public void shouldWatch(){
    given(clientBuilder.mode(AuthenticationMode.SERVICE_ACCOUNT)).willReturn(clientBuilder);
    given(clientBuilder.build()).willReturn(client);
    given(client.events()).willReturn(Flux.just(TaskCreatedEventTest.TASK_CREATED_EVENT));
    watcher.watch();
    verify(eventBus).publish(TaskCreatedEventTest.TASK_CREATED_EVENT);
  }
}