package com.kraken.tools.sse.server;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.client.api.RuntimeClient;
import com.kraken.runtime.client.api.RuntimeClientBuilder;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.storage.client.api.StorageClient;
import com.kraken.storage.client.api.StorageClientBuilder;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.constraints.Pattern;

import static com.kraken.security.authentication.api.AuthenticationMode.SESSION;

@Slf4j
@RestController()
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Validated
public class SSEController {

  @NonNull SSEService sse;
  @NonNull RuntimeClientBuilder runtimeClientBuilder;
  @NonNull StorageClientBuilder storageClientBuilder;

  @GetMapping(value = "/watch")
  public Flux<ServerSentEvent<SSEWrapper>> watch(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId) {
    final var runtimeClient = runtimeClientBuilder.mode(SESSION).applicationId(applicationId).build();
    final var storageClient = storageClientBuilder.mode(SESSION).applicationId(applicationId).build();
    return sse.keepAlive(sse.merge(ImmutableMap.of("NODE", storageClient.watch(), "LOG", runtimeClient
        .watchLogs(), "TASKS", runtimeClient.watchTasks())))
        .map(event -> {
          log.debug(event.toString());
          return event;
        });
  }
}
