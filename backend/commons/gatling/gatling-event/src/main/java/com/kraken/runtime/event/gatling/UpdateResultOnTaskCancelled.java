package com.kraken.runtime.event.gatling;

import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.runtime.event.TaskCancelledEvent;
import com.kraken.tools.event.bus.EventBus;
import com.kraken.tools.event.bus.EventBusListener;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    analysisClient.setStatus(event.getContext().getTaskId(), ResultStatus.CANCELED).subscribe();
  }
}
