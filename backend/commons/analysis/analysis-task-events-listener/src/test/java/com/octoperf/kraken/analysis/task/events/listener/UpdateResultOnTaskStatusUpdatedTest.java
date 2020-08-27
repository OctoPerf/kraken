package com.octoperf.kraken.analysis.task.events.listener;

import com.octoperf.kraken.analysis.entity.ResultStatus;
import com.octoperf.kraken.analysis.service.api.AnalysisService;
import com.octoperf.kraken.runtime.entity.task.ContainerStatus;
import com.octoperf.kraken.runtime.event.TaskStatusUpdatedEvent;
import com.octoperf.kraken.runtime.event.TaskStatusUpdatedEventTest;
import com.octoperf.kraken.tools.event.bus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class UpdateResultOnTaskStatusUpdatedTest {

  @Mock
  AnalysisService analysisService;

  @Mock
  Function<ContainerStatus, ResultStatus> taskStatusToResultStatus;

  @Mock
  EventBus eventBus;

  UpdateResultOnTaskStatusUpdated listener;

  @BeforeEach
  public void before(){
    given(eventBus.of(TaskStatusUpdatedEvent.class)).willReturn(Flux.empty());
    listener = new UpdateResultOnTaskStatusUpdated(eventBus, analysisService, taskStatusToResultStatus);
  }

  @Test
  public void shouldHandleEvent(){
    final var event = TaskStatusUpdatedEventTest.TASK_STATUS_UPDATED_EVENT;
    final var task = event.getTask();
    given(taskStatusToResultStatus.apply(task.getStatus())).willReturn(ResultStatus.STARTING);
    given(analysisService.setResultStatus(any(), any(), any())).willReturn(Mono.empty());
    listener.handleEvent(event);
    verify(analysisService).setResultStatus(task.getOwner(), task.getId(), ResultStatus.STARTING);
  }

}
