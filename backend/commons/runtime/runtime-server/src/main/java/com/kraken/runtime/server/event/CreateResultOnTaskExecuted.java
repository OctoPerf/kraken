package com.kraken.runtime.server.event;

import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultType;
import com.kraken.runtime.event.TaskExecutedEvent;
import com.kraken.tools.event.bus.EventBus;
import com.kraken.tools.event.bus.EventBusListener;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class CreateResultOnTaskExecuted extends EventBusListener<TaskExecutedEvent> {

  AnalysisClient analysisClient;
  Function<String, ResultType> taskTypeToResultType;

  @Autowired
  CreateResultOnTaskExecuted(final EventBus eventBus,
                             final AnalysisClient analysisClient,
                             final Function<String, ResultType> taskTypeToResultType) {
    super(eventBus, TaskExecutedEvent.class);
    this.analysisClient = requireNonNull(analysisClient);
    this.taskTypeToResultType = requireNonNull(taskTypeToResultType);
  }

  @Override
  protected void handleEvent(TaskExecutedEvent event) {
    final var context = event.getContext();
    final var result = Result.builder()
        .id(context.getTaskId())
        .startDate(new Date().getTime())
        .endDate(0L)
        .status(ResultStatus.STARTING)
        .description(context.getDescription())
        .type(taskTypeToResultType.apply(context.getTaskType()))
        .build();
    analysisClient.create(result).subscribe();
  }
}
