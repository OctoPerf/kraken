package com.kraken.tools.sse.server;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.storage.client.StorageClient;
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
@RequestMapping("/sse")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Validated
public class SSEController {

  @NonNull SSEService sse;

  @NonNull RuntimeClient runtimeClient;

  @NonNull StorageClient storageClient;

  @GetMapping(value = "/watch/{applicationId}")
  public Flux<ServerSentEvent<SSEWrapper>> watch(@PathVariable("applicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId) {
    return sse.keepAlive(sse.merge(ImmutableMap.of("NODE", storageClient.watch(), "LOG", runtimeClient.watchLogs(applicationId), "TASKS", runtimeClient.watchTasks(applicationId))));
  }
}
