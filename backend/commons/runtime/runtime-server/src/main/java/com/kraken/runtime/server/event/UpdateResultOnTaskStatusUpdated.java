package com.kraken.runtime.server.event;

import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.event.TaskStatusUpdatedEvent;
import com.kraken.tools.event.bus.EventBus;
import com.kraken.tools.event.bus.EventBusListener;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class UpdateResultOnTaskStatusUpdated extends EventBusListener<TaskStatusUpdatedEvent> {

  AnalysisClient analysisClient;
  Function<ContainerStatus, ResultStatus> taskStatusToResultStatus;

  @Autowired
  UpdateResultOnTaskStatusUpdated(final EventBus eventBus,
                                  final AnalysisClient analysisClient,
                                  final Function<ContainerStatus, ResultStatus> taskStatusToResultStatus) {
    super(eventBus, TaskStatusUpdatedEvent.class);
    this.analysisClient = requireNonNull(analysisClient);
    this.taskStatusToResultStatus = requireNonNull(taskStatusToResultStatus);
  }

  @Override
  protected void handleEvent(TaskStatusUpdatedEvent event) {
    final var task = event.getTask();
    final var resultId = task.getId();
    final var status = taskStatusToResultStatus.apply(task.getStatus());
    log.info(String.format("Set status %s for result %s", status.toString(), resultId));
    analysisClient.setStatus(resultId, status).subscribe();
  }
}
