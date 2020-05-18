package com.kraken.analysis.task.events.watcher;

import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.server.service.AnalysisService;
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

  AnalysisService analysisService;

  @Autowired
  UpdateResultOnTaskCancelled(final EventBus eventBus,
                              final AnalysisService analysisService) {
    super(eventBus, TaskCancelledEvent.class);
    this.analysisService = requireNonNull(analysisService);
  }

  @Override
  protected void handleEvent(TaskCancelledEvent event) {
    analysisService.setStatus(event.getContext().getOwner(), event.getContext().getTaskId(), ResultStatus.CANCELED).subscribe();
  }
}
