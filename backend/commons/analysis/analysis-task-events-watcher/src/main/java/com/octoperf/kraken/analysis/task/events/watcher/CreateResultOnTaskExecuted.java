package com.octoperf.kraken.analysis.task.events.watcher;

import com.octoperf.kraken.analysis.entity.Result;
import com.octoperf.kraken.analysis.entity.ResultStatus;
import com.octoperf.kraken.analysis.entity.ResultType;
import com.octoperf.kraken.analysis.server.service.AnalysisService;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.runtime.event.TaskExecutedEvent;
import com.octoperf.kraken.tools.event.bus.EventBus;
import com.octoperf.kraken.tools.event.bus.EventBusListener;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class CreateResultOnTaskExecuted extends EventBusListener<TaskExecutedEvent> {

  AnalysisService analysisService;
  Function<TaskType, ResultType> taskTypeToResultType;

  @Autowired
  CreateResultOnTaskExecuted(final EventBus eventBus,
                             @NonNull final AnalysisService analysisService,
                             @NonNull final Function<TaskType, ResultType> taskTypeToResultType) {
    super(eventBus, TaskExecutedEvent.class);
    this.analysisService = analysisService;
    this.taskTypeToResultType = taskTypeToResultType;
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
    log.info(String.format("Create result %s", context.getTaskId()));
    analysisService.create(event.getContext().getOwner(), result).subscribe();
  }
}
