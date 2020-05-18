package com.kraken.analysis.task.events.watcher;

import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.server.service.AnalysisService;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.event.TaskStatusUpdatedEvent;
import com.kraken.runtime.event.TaskStatusUpdatedEventTest;
import com.kraken.tools.event.bus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class UpdateResultOnTaskStatusUpdatedTest {

  @Mock
  AnalysisService analysisService;

  @Mock
  Function<ContainerStatus, ResultStatus> taskStatusToResultStatus;

  @Mock
  EventBus eventBus;

  UpdateResultOnTaskStatusUpdated listener;

  @Before
  public void before(){
    given(eventBus.of(TaskStatusUpdatedEvent.class)).willReturn(Flux.empty());
    listener = new UpdateResultOnTaskStatusUpdated(eventBus, analysisService, taskStatusToResultStatus);
  }

  @Test
  public void shouldHandleEvent(){
    final var event = TaskStatusUpdatedEventTest.TASK_STATUS_UPDATED_EVENT;
    final var task = event.getTask();
    given(taskStatusToResultStatus.apply(task.getStatus())).willReturn(ResultStatus.STARTING);
    given(analysisService.setStatus(any(), any(), any())).willReturn(Mono.empty());
    listener.handleEvent(event);
    verify(analysisService).setStatus(task.getOwner(), task.getId(), ResultStatus.STARTING);
  }

}
