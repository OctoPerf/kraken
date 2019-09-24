package com.kraken.runtime.server.service;

import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultType;
import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        .flatMap(this::setResultsStatuses)
        .retryBackoff(Integer.MAX_VALUE, Duration.ofSeconds(5))
        .onErrorResume(throwable -> Mono.empty())
        .subscribe();
  }

  @Override
  public Mono<String> taskExecuted(final String taskId, final TaskType taskType, final Map<String, String> environment) {
    final var result = Result.builder()
        .id(taskId)
        .startDate(new Date().getTime())
        .endDate(0L)
        .status(ResultStatus.STARTING)
        .description(environment.get("KRAKEN_DESCRIPTION"))
        .type(taskTypeToResultType.apply(taskType))
        .build();
    return analysisClient.create(result).map(storageNode -> taskId);
  }

  @Override
  public Mono<String> taskCanceled(final String taskId) {
    return analysisClient.setStatus(taskId, ResultStatus.CANCELED).map(storageNode -> taskId);
  }

  private Flux<StorageNode> setResultsStatuses(final List<Task> tasks) {
    log.info(String.format("Tasks list changed %s", tasks));
    return Flux.concat(tasks.stream().map(this::setResultStatus).collect(Collectors.toList()));
  }

  private Mono<StorageNode> setResultStatus(final Task task) {
    final var resultId = task.getId();
    final var status = taskStatusToResultStatus.apply(task.getStatus());
    log.info(String.format("Set status %s for result %s", status.toString(), resultId));
    return analysisClient.setStatus(resultId, status);
  }
}
