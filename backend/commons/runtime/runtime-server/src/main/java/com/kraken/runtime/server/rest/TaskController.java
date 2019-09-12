package com.kraken.runtime.server.rest;

import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.client.AnalysisClientProperties;
import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.server.service.ResultUpdater;
import com.kraken.storage.client.StorageClientProperties;
import com.kraken.tools.sse.SSEService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController()
@RequestMapping("/task")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class TaskController {

  @NonNull StorageClientProperties storageClientProperties;
  @NonNull AnalysisClientProperties analysisClientProperties;
  @NonNull ResultUpdater resultUpdater;
  @NonNull TaskService service;
  @NonNull SSEService sse;

  @PostMapping("/{type}")
  public Mono<String> run(@RequestHeader("ApplicationId") final String applicationId,
                          @PathVariable("type") final TaskType type,
                          @RequestParam("description") final String description,
                          @RequestBody() final Map<String, String> environment) {
    log.info(String.format("Run task %s", type));
    final var env = ImmutableMap.<String, String>builder()
        .putAll(environment)
        .put("KRAKEN_ANALYSIS_URL", analysisClientProperties.getAnalysisUrl())
        .put("KRAKEN_STORAGE_URL", storageClientProperties.getStorageUrl())
        .build();
    return service.execute(applicationId, type, description, env).flatMap(taskId -> resultUpdater.taskExecuted(taskId, type, description));
  }

  @PostMapping("/cancel")
  public Mono<String> cancel(@RequestHeader("ApplicationId") final String applicationId,
                           @RequestBody() final Task task) {
    log.info(String.format("Cancel task %s", task.getId()));
    return service.cancel(applicationId, task).flatMap(aVoid -> resultUpdater.taskCanceled(task.getId()));
  }

  @GetMapping(value = "/watch")
  public Flux<ServerSentEvent<List<Task>>> watch() {
    log.info("Watch tasks lists");
    return sse.keepAlive(service.watch());
  }

  @GetMapping(value = "/list")
  public Flux<Task> list() {
    return service.list();
  }
}
