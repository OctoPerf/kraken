package com.kraken.analysis.task.events.watcher;

import com.kraken.runtime.event.TaskCreatedEventTest;
import com.kraken.runtime.event.client.api.RuntimeEventClient;
import com.kraken.runtime.event.client.api.RuntimeEventClientBuilder;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.tools.event.bus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TaskEventsWatcherTest {

  @Mock
  RuntimeEventClientBuilder clientBuilder;

  @Mock
  RuntimeEventClient client;
  @Mock
  EventBus eventBus;

  TaskEventsWatcher watcher;

  @Before
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