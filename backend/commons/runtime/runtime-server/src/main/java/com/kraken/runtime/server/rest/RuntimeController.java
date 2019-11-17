package com.kraken.runtime.server.rest;

import com.kraken.runtime.logs.LogsService;
import com.kraken.runtime.server.service.TaskListService;
import com.kraken.tools.sse.SSEService;
import com.kraken.tools.sse.SSEWrapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.constraints.Pattern;

@Slf4j
@RestController()
@RequestMapping("/runtime")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Validated
public class RuntimeController {

  @NonNull LogsService logsService;
  @NonNull TaskListService taskListService;

  @NonNull
  SSEService sse;

  @GetMapping(value = "/watch/{applicationId}")
  public Flux<ServerSentEvent<SSEWrapper>> watch(@PathVariable("applicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId) {
    return sse.keepAlive(sse.merge("LOG", logsService.listen(applicationId), "TASKS", taskListService.watch()));
  }
}
