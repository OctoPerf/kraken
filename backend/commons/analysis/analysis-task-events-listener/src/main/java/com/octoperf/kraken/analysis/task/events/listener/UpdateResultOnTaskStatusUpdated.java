package com.octoperf.kraken.analysis.task.events.listener;

import com.octoperf.kraken.analysis.entity.ResultStatus;
import com.octoperf.kraken.analysis.service.api.AnalysisService;
import com.octoperf.kraken.runtime.entity.task.ContainerStatus;
import com.octoperf.kraken.runtime.event.TaskStatusUpdatedEvent;
import com.octoperf.kraken.tools.event.bus.EventBus;
import com.octoperf.kraken.tools.event.bus.EventBusListener;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;


@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class UpdateResultOnTaskStatusUpdated extends EventBusListener<TaskStatusUpdatedEvent> {

  AnalysisService analysisService;
  Function<ContainerStatus, ResultStatus> taskStatusToResultStatus;

  @Autowired
  UpdateResultOnTaskStatusUpdated(final EventBus eventBus,
                                  @NonNull final AnalysisService analysisService,
                                  @NonNull final Function<ContainerStatus, ResultStatus> taskStatusToResultStatus) {
    super(eventBus, TaskStatusUpdatedEvent.class);
    this.analysisService = analysisService;
    this.taskStatusToResultStatus = taskStatusToResultStatus;
  }

  @Override
  protected void handleEvent(TaskStatusUpdatedEvent event) {
    final var task = event.getTask();
    final var resultId = task.getId();
    final var status = taskStatusToResultStatus.apply(task.getStatus());
    log.info(String.format("Set status %s for result %s", status.toString(), resultId));
    analysisService.setResultStatus(event.getTask().getOwner(), resultId, status).subscribe();
  }
}
