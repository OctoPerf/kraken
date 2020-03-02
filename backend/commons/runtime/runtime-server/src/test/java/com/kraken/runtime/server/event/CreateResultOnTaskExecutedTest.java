package com.kraken.runtime.server.event;

import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultType;
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
public class CreateResultOnTaskExecutedTest {

  @Mock
  AnalysisClient analysisClient;

  @Mock
  Function<String, ResultType> taskTypeToResultType;

  @Mock
  EventBus eventBus;

  @Captor
  ArgumentCaptor<Result> resultArgumentCaptor;

  CreateResultOnTaskExecuted listener;

  @Before
  public void before(){
    given(eventBus.of(TaskExecutedEvent.class)).willReturn(Flux.empty());
    listener = new CreateResultOnTaskExecuted(eventBus, analysisClient, taskTypeToResultType);
  }

  @Test
  public void shouldHandleEvent(){
    final var event = TaskExecutedEventTest.TASK_EXECUTED_EVENT;
    final var context = event.getContext();
    given(taskTypeToResultType.apply(context.getTaskType())).willReturn(ResultType.RUN);
    given(analysisClient.create(any())).willReturn(Mono.empty());
    listener.handleEvent(event);
    verify(analysisClient).create(resultArgumentCaptor.capture());
    final var result = resultArgumentCaptor.getValue();
    assertThat(result.getId()).isEqualTo(context.getTaskId());
    assertThat(result.getEndDate()).isEqualTo(0L);
    assertThat(result.getStatus()).isEqualTo(ResultStatus.STARTING);
    assertThat(result.getDescription()).isEqualTo(context.getDescription());
    assertThat(result.getType()).isEqualTo(ResultType.RUN);
  }

}
