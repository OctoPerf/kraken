package com.kraken.runtime.server.service;

import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultType;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.ExecutionContext;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.logs.LogsService;
import com.kraken.storage.entity.StorageNode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class SpringTaskUpdateHandler implements TaskUpdateHandler {

  @NonNull TaskListService taskListService;
  @NonNull AnalysisClient analysisClient;
  @NonNull Function<TaskType, ResultType> taskTypeToResultType;
  @NonNull Function<ContainerStatus, ResultStatus> taskStatusToResultStatus;
  @NonNull LogsService logsService;

  @PostConstruct
  public void start() {
    taskListService.watch()
        .flatMap(this::setResultsStatuses)
        .retryBackoff(Integer.MAX_VALUE, Duration.ofSeconds(5))
        .onErrorResume(throwable -> Mono.empty())
        .subscribe();
  }

  @Override
  public Mono<String> taskExecuted(final ExecutionContext context) {
    final var result = Result.builder()
        .id(context.getTaskId())
        .startDate(new Date().getTime())
        .endDate(0L)
        .status(ResultStatus.STARTING)
        .description(context.getDescription())
        .type(taskTypeToResultType.apply(context.getTaskType()))
        .build();
    return analysisClient.create(result).map(storageNode -> context.getTaskId());
  }

  @Override
  public Mono<String> taskCanceled(final String taskId) {
    return analysisClient.setStatus(taskId, ResultStatus.CANCELED).map(storageNode -> taskId);
  }

  @Override
  public Flux<List<Task>> scanForUpdates() {
    return taskListService.watch()
        .scan((previousTasks, currentTasks) -> {
          final var previousMap = previousTasks.stream().collect(Collectors.toMap(Task::getId, Function.identity()));
          final var currentMap = currentTasks.stream().collect(Collectors.toMap(Task::getId, Function.identity()));
          previousTasks.stream().filter(task -> !currentMap.containsKey(task.getId())).forEach(task -> this.taskRemoved(task).subscribe());
          currentTasks.stream().filter(task -> !previousMap.containsKey(task.getId())).forEach(task -> this.taskCreated(task).subscribe());
          currentTasks.stream().filter(task -> previousMap.containsKey(task.getId()))
              .filter(task -> previousMap.get(task.getId()).getStatus() != task.getStatus())
              .forEach(task -> this.taskStatusUpdated(task).subscribe());
          return currentTasks;
        });
  }

  @Override
  public Mono<Void> taskCreated(Task task) {
    if (task.getStatus() != ContainerStatus.DONE) {
      return setResultStatus(task).then();
    }
    return Mono.empty();
  }

  @Override
  public Mono<Void> taskStatusUpdated(Task task) {
    if (task.getStatus() == ContainerStatus.DONE) {
      //logsService.dispose();
    }
    return setResultStatus(task).then();
  }

  @Override
  public Mono<Void> taskRemoved(Task task) {
    return Mono.fromCallable(() -> {
//      TODO ajouter l'applicationId sur les taches et containers ... FUUUU
//      TODO ou se demerder pour que le logService envoie bien un log de fin dans tous les cas
//      logsService.dispose(task.)
      return null;
    });
  }

  private Flux<StorageNode> setResultsStatuses(final List<Task> tasks) {
    log.info(String.format("Tasks list changed %s", tasks));
    return Flux.concat(tasks.stream().map(this::setResultStatus).collect(Collectors.toList()));
  }

  private Mono<StorageNode> setResultStatus(final Task task) {
    final var resultId = task.getId();
    final var status = taskStatusToResultStatus.apply(task.getStatus());
    log.info(String.format("Set status %s for result %s", status.toString(), resultId));
    return analysisClient.setStatus(resultId, status).onErrorResume(throwable -> Mono.empty());
  }
}
