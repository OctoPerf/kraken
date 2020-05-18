package com.kraken.runtime.server.rest;

import com.kraken.runtime.entity.log.Log;
import com.kraken.runtime.logs.LogsService;
import com.kraken.runtime.server.service.TaskListService;
import com.kraken.security.authentication.api.UserProvider;
import com.kraken.tools.sse.SSEService;
import com.kraken.tools.sse.SSEWrapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.constraints.Pattern;
import java.util.Optional;

import static java.util.Optional.of;

@Slf4j
@RestController()
@RequestMapping("/logs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Validated
public class LogsController {

  @NonNull LogsService logsService;
  @NonNull UserProvider userProvider;

  @NonNull
  SSEService sse;

  @GetMapping(value = "/watch")
  public Flux<ServerSentEvent<Log>> watch(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId) {
    return userProvider.getOwner(applicationId).flatMapMany(owner -> sse.keepAlive(logsService.listen(owner)));
  }
}
