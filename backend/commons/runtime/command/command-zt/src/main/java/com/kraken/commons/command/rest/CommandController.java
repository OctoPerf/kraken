package com.kraken.commons.command.rest;

import com.kraken.commons.command.entity.Command;
import com.kraken.commons.command.entity.CommandLog;
import com.kraken.commons.command.executor.CommandExecutor;
import com.kraken.commons.sse.SSEService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController()
@RequestMapping()
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class CommandController {

  @NonNull
  CommandExecutor executor;

  @NonNull
  SSEService sse;

  @PostMapping("/execute")
  public Mono<String> run(@RequestBody final Command command) {
    return this.executor.execute(command);
  }

  // ApplicationId Header cannot be used here as it is not supported by SSE
  @GetMapping("/listen/{applicationId}")
  public Flux<ServerSentEvent<CommandLog>> listen(@PathVariable("applicationId") final String applicationId) {
    return this.sse.wrap(this.executor.listen(applicationId));
  }

  @DeleteMapping("/cancel")
  public Mono<Boolean> cancel(@RequestParam("commandId") final String commandId) {
    return Mono.just(this.executor.cancel(commandId));
  }
}
