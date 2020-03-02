package com.runtime.context.entity;

import com.google.common.collect.ImmutableMap;
import lombok.*;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.stream.Collectors;

@Value
@Builder
@AllArgsConstructor
public class ExecutionContext {
  @NonNull String applicationId;
  @NonNull String taskId;
  @NonNull String taskType;
  @NonNull String description;
  @NonNull String file;
  //  Key: hostId, Value; env specific to this host
  @With
  @NonNull Map<String, Map<String, String>> hostEnvironments;

  public ExecutionContext withGlobalEnvironmentVariable(final String key, final String value) {
    return Flux.fromIterable(this.hostEnvironments.keySet())
        .reduce(this, (context, hostId) -> context.withHostEnvironmentVariable(hostId, key, value)).block();
  }

  public ExecutionContext withHostEnvironmentVariable(final String hostId, final String key, final String value) {
    if (this.hostEnvironments.containsKey(hostId)) {
      final var hostEnv = this.hostEnvironments.get(hostId);
      if (hostEnv.containsKey(key)) {
        // Do not override values
        return this;
      }
      final var envBuilder = ImmutableMap.<String, String>builder();
      envBuilder.putAll(hostEnv);
      envBuilder.put(key, value);
      return withHostEnvironment(hostId, envBuilder.build());
    }
    return withHostEnvironment(hostId, ImmutableMap.of(key, value));
  }

  private ExecutionContext withHostEnvironment(final String hostId, Map<String, String> hostEnvironment) {
    final var envBuilder = ImmutableMap.<String, Map<String, String>>builder();
    envBuilder.putAll(this.hostEnvironments.entrySet().stream().filter(currentHostEnv -> !currentHostEnv.getKey().equals(hostId)).collect(Collectors.toList()));
    envBuilder.put(hostId, hostEnvironment);
    return this.withHostEnvironments(envBuilder.build());
  }
}
