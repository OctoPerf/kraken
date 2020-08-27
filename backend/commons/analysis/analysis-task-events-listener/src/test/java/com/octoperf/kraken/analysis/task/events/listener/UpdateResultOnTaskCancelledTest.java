package com.octoperf.kraken.analysis.task.events.listener;

import com.octoperf.kraken.analysis.entity.ResultStatus;
import com.octoperf.kraken.analysis.service.api.AnalysisService;
import com.octoperf.kraken.runtime.event.TaskCancelledEvent;
import com.octoperf.kraken.runtime.event.TaskCancelledEventTest;
import com.octoperf.kraken.tools.event.bus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UpdateResultOnTaskCancelledTest {

  @Mock
  AnalysisService analysisService;

  @Mock
  EventBus eventBus;

  UpdateResultOnTaskCancelled listener;

  @BeforeEach
  public void before(){
    given(eventBus.of(TaskCancelledEvent.class)).willReturn(Flux.empty());
    listener = new UpdateResultOnTaskCancelled(eventBus, analysisService);
  }

  @Test
  public void shouldHandleEvent(){
    final var event = TaskCancelledEventTest.TASK_CANCELLED_EVENT;
    given(analysisService.setResultStatus(any(),any(),any())).willReturn(Mono.empty());
    listener.handleEvent(event);
    verify(analysisService).setResultStatus(event.getContext().getOwner(), event.getContext().getTaskId(), ResultStatus.CANCELED);
  }

}
