package com.kraken.runtime.server.event;

import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultType;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.event.TaskCancelledEvent;
import com.kraken.runtime.event.TaskExecutedEvent;
import com.kraken.tools.event.bus.EventBus;
import com.kraken.tools.event.bus.EventBusListener;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class UpdateResultOnTaskCancelled extends EventBusListener<TaskCancelledEvent> {

  AnalysisClient analysisClient;

  @Autowired
  UpdateResultOnTaskCancelled(final EventBus eventBus,
                              final AnalysisClient analysisClient) {
    super(eventBus, TaskCancelledEvent.class);
    this.analysisClient = requireNonNull(analysisClient);
  }

  @Override
  protected void handleEvent(TaskCancelledEvent event) {
    analysisClient.setStatus(event.getTaskId(), ResultStatus.CANCELED).subscribe();
  }
}
