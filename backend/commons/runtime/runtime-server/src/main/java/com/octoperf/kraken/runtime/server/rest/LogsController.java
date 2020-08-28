package com.octoperf.kraken.runtime.server.rest;

import com.octoperf.kraken.runtime.entity.log.Log;
import com.octoperf.kraken.runtime.logs.LogsService;
import com.octoperf.kraken.security.authentication.api.UserProvider;
import com.octoperf.kraken.tools.sse.SSEService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.constraints.Pattern;

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
  public Flux<ServerSentEvent<Log>> watch(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                          @RequestHeader("ProjectId") @Pattern(regexp = "[a-z0-9]{10}") final String projectId) {
    return userProvider.getOwner(applicationId, projectId).flatMapMany(owner -> sse.keepAlive(logsService.listen(owner)));
  }
}
