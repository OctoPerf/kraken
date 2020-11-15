package com.octoperf.kraken.git.service.cmd;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.git.entity.GitStatus;
import com.octoperf.kraken.git.event.GitRefreshStorageEvent;
import com.octoperf.kraken.git.event.GitStatusUpdateEvent;
import com.octoperf.kraken.git.service.api.GitCommandService;
import com.octoperf.kraken.git.service.api.GitLogsService;
import com.octoperf.kraken.git.service.api.GitProjectService;
import com.octoperf.kraken.git.service.cmd.parser.GitStatusParser;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.tools.event.bus.EventBus;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableMap.of;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class CmdGitCommandService implements GitCommandService {

  private static final int MAX_EVENTS_SIZE = 10;
  private static final Duration MAX_EVENTS_TIMEOUT_MS = Duration.ofMillis(1000);

  @NonNull OwnerToPath ownerToPath;
  @NonNull CommandService commandService;
  @NonNull GitLogsService logsService;
  @NonNull GitProjectService projectService;
  @NonNull GitStatusParser statusParser;
  @NonNull EventBus eventBus;

  @Override
  public Mono<Void> execute(final Owner owner, final String command) {
    return commandService.parseCommandLine(command)
        .flatMap(args -> Mono.fromCallable(() -> {
          checkArgument(args.size() >= 2);
          checkArgument("git".equals(args.get(0)), "Only git commands are supported.");
          final var subCommand = GitSubCommand.valueOf(args.get(1).toUpperCase());
          final var cmd = Command.builder()
              .args(args)
              .path(ownerToPath.apply(owner).toString())
              .environment(of())
              .build();
          final var logsFlux = commandService.validate(cmd)
              .flatMapMany(commandService::execute)
              .doOnComplete(() -> {
                if (subCommand.isRefresh()) {
                  eventBus.publish(GitRefreshStorageEvent.builder().owner(owner).build());
                }
                eventBus.publish(GitStatusUpdateEvent.builder().owner(owner).build());
              });
          logsService.push(owner, logsFlux);
          return null;
        }));
  }

  @Override
  public Mono<GitStatus> status(final Owner owner) {
    if (projectService.isConnected(owner)) {
      final var commandStatus = Command.builder()
          .args(ImmutableList.of("git", "--no-optional-locks", "status", "--porcelain=v2", "--branch"))
          .path(ownerToPath.apply(owner).toString())
          .environment(of())
          .build();
      return statusParser.apply(commandService.validate(commandStatus).flatMapMany(commandService::execute));
    }
    return Mono.empty();
  }

  @Override
  public Flux<GitStatus> watchStatus(final Owner owner) {
    if (projectService.isConnected(owner)) {
      return Flux.concat(this.status(owner), this.eventBus.of(GitStatusUpdateEvent.class)
          .filter(event -> event.getOwner().equals(owner))
          .sampleTimeout(event -> Mono.delay(MAX_EVENTS_TIMEOUT_MS), MAX_EVENTS_SIZE)
          .flatMap(window -> this.status(owner)));
    }
    return Flux.empty();
  }

  @Override
  public Flux<GitRefreshStorageEvent> watchRefresh(final Owner owner) {
    if (projectService.isConnected(owner)) {
      return this.eventBus.of(GitRefreshStorageEvent.class)
          .filter(event -> event.getOwner().equals(owner))
          .sampleTimeout(event -> Mono.delay(MAX_EVENTS_TIMEOUT_MS), MAX_EVENTS_SIZE);
    }
    return Flux.empty();
  }

}
