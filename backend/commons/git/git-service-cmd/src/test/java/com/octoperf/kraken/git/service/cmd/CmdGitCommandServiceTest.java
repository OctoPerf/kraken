package com.octoperf.kraken.git.service.cmd;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.git.entity.GitStatusTest;
import com.octoperf.kraken.git.event.GitRefreshStorageEvent;
import com.octoperf.kraken.git.event.GitStatusUpdateEvent;
import com.octoperf.kraken.git.service.api.GitLogsService;
import com.octoperf.kraken.git.service.api.GitProjectService;
import com.octoperf.kraken.git.service.cmd.parser.GitStatusParser;
import com.octoperf.kraken.security.entity.owner.OwnerTest;
import com.octoperf.kraken.tools.event.bus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CmdGitCommandServiceTest {

  @Mock
  OwnerToPath ownerToPath;
  @Mock
  CommandService commandService;
  @Mock
  GitLogsService logsService;
  @Mock
  GitProjectService projectService;
  @Mock
  GitStatusParser statusParser;
  @Mock
  EventBus eventBus;

  CmdGitCommandService service;

  @BeforeEach
  void setUp() {
    service = new CmdGitCommandService(ownerToPath,
        commandService,
        logsService,
        projectService,
        statusParser,
        eventBus);
  }

  @Test
  void shouldGetStatus() {
    mockStatus();
    given(projectService.isConnected(OwnerTest.USER_OWNER)).willReturn(true);
    final var status = service.status(OwnerTest.USER_OWNER).block();
    assertThat(status).isNotNull().isEqualTo(GitStatusTest.GIT_STATUS);
    verify(commandService).validate(Command.builder()
        .args(ImmutableList.of("git", "--no-optional-locks", "status", "--porcelain=v2", "--branch"))
        .path("path")
        .environment(ImmutableMap.of())
        .build());
  }

  @Test
  void shouldGetStatusEmpty() {
    given(projectService.isConnected(OwnerTest.USER_OWNER)).willReturn(false);
    final var status = service.status(OwnerTest.USER_OWNER).block();
    assertThat(status).isNull();
  }

  private void mockStatus() {
    given(commandService.validate(any(Command.class))).willAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0)));
    given(statusParser.apply(any())).willReturn(Mono.just(GitStatusTest.GIT_STATUS));
    given(ownerToPath.apply(any())).willReturn(Path.of("path"));
  }

  @Test
  void shouldWatchStatus() {
    mockStatus();
    given(projectService.isConnected(OwnerTest.USER_OWNER)).willReturn(true);
    given(eventBus.of(GitStatusUpdateEvent.class)).willReturn(Flux.range(0, 299).map(integer -> GitStatusUpdateEvent.builder().owner(OwnerTest.USER_OWNER).build()));
    final var statuses = service.watchStatus(OwnerTest.USER_OWNER).collectList().block();
    assertThat(statuses).isNotNull().hasSize(2);
  }

  @Test
  void shouldWatchRefresh() {
    given(projectService.isConnected(OwnerTest.USER_OWNER)).willReturn(true);
    given(eventBus.of(GitRefreshStorageEvent.class)).willReturn(Flux.range(0, 299).map(integer -> GitRefreshStorageEvent.builder().owner(OwnerTest.USER_OWNER).build()));
    final var refreshes = service.watchRefresh(OwnerTest.USER_OWNER).collectList().block();
    assertThat(refreshes).isNotNull().hasSize(1);
  }

  @Test
  void shouldWatchStatusEmpty() {
    given(projectService.isConnected(OwnerTest.USER_OWNER)).willReturn(false);
    final var statuses = service.watchStatus(OwnerTest.USER_OWNER).collectList().block();
    assertThat(statuses).isNotNull().isEmpty();
  }

  @Test
  void shouldWatchRefreshEmpty() {
    given(projectService.isConnected(OwnerTest.USER_OWNER)).willReturn(false);
    final var refreshes = service.watchRefresh(OwnerTest.USER_OWNER).collectList().block();
    assertThat(refreshes).isNotNull().isEmpty();
  }

  @Test
  void shouldExecute() {
    final var commandLine = "git status";
    final var args = ImmutableList.of("git", "status");
    final var path = Path.of("path");
    given(ownerToPath.apply(OwnerTest.USER_OWNER)).willReturn(path);
    given(commandService.parseCommandLine(commandLine)).willReturn(Mono.just(args));
    given(commandService.validate(any(Command.class))).willAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0)));

    service.execute(OwnerTest.USER_OWNER, commandLine).block();
    verify(logsService).push(any(), any());
    verify(commandService).validate(Command.builder()
        .args(args)
        .path(path.toString())
        .environment(ImmutableMap.of())
        .build());
  }

  @Test
  void shouldExecuteRefresh() {
    final var commandLine = "git rm";
    final var args = ImmutableList.of("git", "rm");
    final var path = Path.of("path");
    given(ownerToPath.apply(OwnerTest.USER_OWNER)).willReturn(path);
    given(commandService.parseCommandLine(commandLine)).willReturn(Mono.just(args));
    given(commandService.validate(any(Command.class))).willAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0)));
    given(commandService.execute(any(Command.class))).willReturn(Flux.just("logs"));

    given(logsService.push(any(), any())).willAnswer(invocationOnMock -> invocationOnMock.getArgument(1, Flux.class).subscribe());

    service.execute(OwnerTest.USER_OWNER, commandLine).block();
    verify(logsService).push(any(), any());
    verify(commandService).validate(Command.builder()
        .args(args)
        .path(path.toString())
        .environment(ImmutableMap.of())
        .build());
    verify(eventBus).publish(GitRefreshStorageEvent.builder().owner(OwnerTest.USER_OWNER).build());
  }
}