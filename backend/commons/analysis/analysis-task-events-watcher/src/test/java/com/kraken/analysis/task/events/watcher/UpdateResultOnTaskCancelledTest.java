package com.kraken.analysis.task.events.watcher;

import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.server.service.AnalysisService;
import com.kraken.runtime.event.TaskCancelledEvent;
import com.kraken.runtime.event.TaskCancelledEventTest;
import com.kraken.tools.event.bus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UpdateResultOnTaskCancelledTest {

  @Mock
  AnalysisService analysisService;

  @Mock
  EventBus eventBus;

  UpdateResultOnTaskCancelled listener;

  @Before
  public void before(){
    given(eventBus.of(TaskCancelledEvent.class)).willReturn(Flux.empty());
    listener = new UpdateResultOnTaskCancelled(eventBus, analysisService);
  }

  @Test
  public void shouldHandleEvent(){
    final var event = TaskCancelledEventTest.TASK_CANCELLED_EVENT;
    given(analysisService.setStatus(any(),any(),any())).willReturn(Mono.empty());
    listener.handleEvent(event);
    verify(analysisService).setStatus(event.getContext().getOwner(), event.getContext().getTaskId(), ResultStatus.CANCELED);
  }

}
