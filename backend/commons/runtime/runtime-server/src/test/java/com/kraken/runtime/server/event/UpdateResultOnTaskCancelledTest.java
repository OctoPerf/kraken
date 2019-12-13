package com.kraken.runtime.server.event;

import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultType;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.event.TaskCancelledEvent;
import com.kraken.runtime.event.TaskCancelledEventTest;
import com.kraken.runtime.event.TaskExecutedEvent;
import com.kraken.runtime.event.TaskExecutedEventTest;
import com.kraken.tools.event.bus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class UpdateResultOnTaskCancelledTest {

  @Mock
  AnalysisClient analysisClient;

  @Mock
  EventBus eventBus;

  UpdateResultOnTaskCancelled listener;

  @Before
  public void before(){
    given(eventBus.of(TaskCancelledEvent.class)).willReturn(Flux.empty());
    listener = new UpdateResultOnTaskCancelled(eventBus, analysisClient);
  }

  @Test
  public void shouldHandleEvent(){
    final var event = TaskCancelledEventTest.TASK_CANCELLED_EVENT;
    given(analysisClient.setStatus(any(),any())).willReturn(Mono.empty());
    listener.handleEvent(event);
    verify(analysisClient).setStatus(event.getTaskId(), ResultStatus.CANCELED);
  }

}
