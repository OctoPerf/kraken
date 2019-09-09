package com.kraken.runtime.server.service;

import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultType;
import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class SpringResultUpdater implements ResultUpdater {

  @NonNull TaskService taskService;
  @NonNull AnalysisClient analysisClient;
  @NonNull Function<TaskType, ResultType> taskTypeToResultType;
  @NonNull Function<ContainerStatus, ResultStatus> taskStatusToResultStatus;

  @PostConstruct
  public void start() {
    taskService.watch()
        .onErrorResume(e -> Mono.empty())
        .subscribe(this::updateResults);
  }

  @Override
  public Mono<String> taskExecuted(final String taskId, final TaskType taskType, final String description) {
    final var result = Result.builder()
        .id(taskId)
        .startDate(new Date().getTime())
        .endDate(0L)
        .status(ResultStatus.STARTING)
        .description(description)
        .type(taskTypeToResultType.apply(taskType))
        .build();
    return analysisClient.create(result).map(storageNode -> taskId);
  }

  @Override
  public Mono<String> taskCanceled(final String taskId) {
    return analysisClient.setStatus(taskId, ResultStatus.CANCELED).map(storageNode -> taskId);
  }

  private void updateResults(final List<Task> tasks) {
    Flux.fromIterable(tasks)
        .flatMap(task -> analysisClient.setStatus(task.getId(), taskStatusToResultStatus.apply(task.getStatus())))
        .onErrorResume(e -> Mono.empty())
        .subscribeOn(Schedulers.elastic())
        .subscribe();
  }

}
