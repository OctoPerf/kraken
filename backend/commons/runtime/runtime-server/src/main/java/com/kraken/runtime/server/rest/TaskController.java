package com.kraken.runtime.server.rest;

import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
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

  @NonNull
  TaskService service;

  @NonNull
  SSEService sse;

  @PostMapping("/{type}")
  public Mono<String> run(@RequestHeader("ApplicationId") final String applicationId,
                          @PathVariable("type") TaskType type,
                          @RequestBody() final Map<String, String> environment) {
    return service.execute(applicationId, type, environment);
  }

  @PostMapping("/cancel")
  public Mono<Void> cancel(@RequestHeader("ApplicationId") final String applicationId,
                           @RequestBody() final Task task) {
    return service.cancel(applicationId, task);
  }

  @GetMapping(value = "/watch")
  public Flux<ServerSentEvent<List<Task>>> watch() {
    return sse.keepAlive(service.watch());
  }

  @GetMapping(value = "/list")
  public Flux<Task> list() {
    return service.list();
  }
}
