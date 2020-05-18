package com.kraken.analysis.task.events.watcher;

import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultType;
import com.kraken.analysis.server.service.AnalysisService;
import com.kraken.runtime.entity.task.TaskType;
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

  AnalysisService analysisService;
  Function<TaskType, ResultType> taskTypeToResultType;

  @Autowired
  CreateResultOnTaskExecuted(final EventBus eventBus,
                             final AnalysisService analysisService,
                             final Function<TaskType, ResultType> taskTypeToResultType) {
    super(eventBus, TaskExecutedEvent.class);
    this.analysisService = requireNonNull(analysisService);
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
    analysisService.create(event.getContext().getOwner(), result).subscribe();
  }
}
