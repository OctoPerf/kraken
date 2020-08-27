package com.octoperf.kraken.analysis.task.events.listener;

import com.octoperf.kraken.analysis.entity.Result;
import com.octoperf.kraken.analysis.entity.ResultStatus;
import com.octoperf.kraken.analysis.entity.ResultType;
import com.octoperf.kraken.analysis.service.api.AnalysisService;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.runtime.event.TaskExecutedEvent;
import com.octoperf.kraken.runtime.event.TaskExecutedEventTest;
import com.octoperf.kraken.tools.event.bus.EventBus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class CreateResultOnTaskExecutedTest {

  @Mock
  AnalysisService analysisService;

  @Mock
  Function<TaskType, ResultType> taskTypeToResultType;

  @Mock
  EventBus eventBus;

  @Captor
  ArgumentCaptor<Result> resultArgumentCaptor;

  CreateResultOnTaskExecuted listener;

  @BeforeEach
  public void before(){
    given(eventBus.of(TaskExecutedEvent.class)).willReturn(Flux.empty());
    listener = new CreateResultOnTaskExecuted(eventBus, analysisService, taskTypeToResultType);
  }

  @Test
  public void shouldHandleEvent(){
    final var event = TaskExecutedEventTest.TASK_EXECUTED_EVENT;
    final var context = event.getContext();
    given(taskTypeToResultType.apply(context.getTaskType())).willReturn(ResultType.RUN);
    given(analysisService.createResult(any(), any())).willReturn(Mono.empty());
    listener.handleEvent(event);
    verify(analysisService).createResult(eq(context.getOwner()), resultArgumentCaptor.capture());
    final var result = resultArgumentCaptor.getValue();
    Assertions.assertThat(result.getId()).isEqualTo(context.getTaskId());
    Assertions.assertThat(result.getEndDate()).isEqualTo(0L);
    Assertions.assertThat(result.getStatus()).isEqualTo(ResultStatus.STARTING);
    Assertions.assertThat(result.getDescription()).isEqualTo(context.getDescription());
    Assertions.assertThat(result.getType()).isEqualTo(ResultType.RUN);
  }

}
