package com.kraken.commons.docker.client.compose;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.commons.command.entity.Command;
import com.kraken.commons.command.executor.CommandExecutor;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
final class DockerComposeShellService implements DockerComposeService {

  @NonNull
  CommandExecutor executor;

  @Override
  public Mono<String> up(final String applicationId, final String path) {
    return this.executeAsync(applicationId, path, "up", "-d", "--no-color");
  }

  @Override
  public Mono<String> down(final String applicationId, final String path) {
    return this.executeAsync(applicationId, path, "down");
  }

  @Override
  public Mono<String> ps(final String applicationId, final String path) {
    return this.executeAsync(applicationId, path, "ps");
  }

  @Override
  public Mono<String> logs(final String applicationId, final String path) {
    return this.executeAsync(applicationId, path, "logs", "--no-color");
  }

  private Mono<String> executeAsync(final String applicationId, final String path, final String... command) {
    final var commandBuilder = new ImmutableList.Builder<String>();
    commandBuilder.add("docker-compose");
    commandBuilder.add("--no-ansi");
//    commandBuilder.add("--verbose");
    commandBuilder.addAll(Arrays.asList(command));
    final Command dockerComposeCmd = Command.builder()
        .id("")
        .applicationId(applicationId)
        .command(commandBuilder.build())
        .environment(ImmutableMap.of())
        .path(path)
        .onCancel(ImmutableList.of())
        .build();
    return executor.execute(dockerComposeCmd);
  }
}
