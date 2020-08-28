package com.octoperf.kraken.tools.sse.server;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.runtime.client.api.RuntimeWatchClientBuilder;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.storage.client.api.StorageClientBuilder;
import com.octoperf.kraken.tools.sse.SSEService;
import com.octoperf.kraken.tools.sse.SSEWrapper;
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
import reactor.core.publisher.Mono;

import javax.validation.constraints.Pattern;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.SESSION;

@Slf4j
@RestController()
@RequestMapping("/sse")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Validated
public class SSEController {

  @NonNull SSEService sse;
  @NonNull RuntimeWatchClientBuilder runtimeClientBuilder;
  @NonNull StorageClientBuilder storageClientBuilder;

  @GetMapping(value = "/watch")
  public Flux<ServerSentEvent<SSEWrapper>> watch(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                                 @RequestHeader("ProjectId") @Pattern(regexp = "[a-z0-9]{10}") final String projectId) {
    final var storageClient = storageClientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .mode(SESSION)
        .applicationId(applicationId)
        .projectId(projectId)
        .build());
    final var runtimeClient = runtimeClientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .mode(SESSION)
        .applicationId(applicationId)
        .projectId(projectId)
        .build());
    return Mono.zip(storageClient, runtimeClient)
        .flatMapMany(clients ->
            sse.keepAlive(sse.merge(ImmutableMap.of(
                "NODE", clients.getT1().watch(),
                "LOG", clients.getT2().watchLogs(),
                "TASKS", clients.getT2().watchTasks()
            ))))
        .map(event -> {
          log.debug(event.toString());
          return event;
        });
  }
}
