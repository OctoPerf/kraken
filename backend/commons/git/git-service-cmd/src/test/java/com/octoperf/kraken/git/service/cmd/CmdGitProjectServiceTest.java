package com.octoperf.kraken.git.service.cmd;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.config.api.ApplicationProperties;
import com.octoperf.kraken.git.entity.GitConfiguration;
import com.octoperf.kraken.security.authentication.api.UserProvider;
import com.octoperf.kraken.security.entity.owner.OwnerTest;
import com.octoperf.kraken.security.entity.token.KrakenTokenUserTest;
import com.octoperf.kraken.tools.event.bus.EventBus;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CmdGitProjectServiceTest {

  @Mock
  OwnerToPath ownerToPath;
  @Mock
  CommandService commandService;
  @Mock
  ApplicationProperties properties;
  @Mock
  UserIdToSSH toSSH;
  @Mock
  UserProvider userProvider;
  @Mock
  EventBus eventBus;
  @Captor
  ArgumentCaptor<Command> commandCaptor;

  CmdGitProjectService projectService;
  Path rootPath;
  Path gitPath;

  @BeforeEach
  public void beforeEach() {
    projectService = new CmdGitProjectService(ownerToPath, commandService, properties, toSSH, userProvider, eventBus);
    rootPath = Paths.get("testDir");
    gitPath = rootPath.resolve(".git");
    System.out.println(gitPath.toAbsolutePath().toString());
    System.out.println(gitPath.toFile().mkdirs());
  }

  @After
  public void afterEach() {
    System.out.println(gitPath.toFile().delete());
  }

  @Test
  void shouldConnect() {
    final var owner = OwnerTest.USER_OWNER;
    final var repositoryUrl = "repoUrl";
    given(toSSH.apply(owner.getUserId())).willReturn("ssh -i");
    given(properties.getData()).willReturn("testDir");
    given(userProvider.getAuthenticatedUser()).willReturn(Mono.just(KrakenTokenUserTest.KRAKEN_USER));
    given(ownerToPath.apply(owner)).willReturn(rootPath);
    given(commandService.validate(anyList())).willAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0)));
    given(commandService.validate(any(Command.class))).willAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0)));
    given(commandService.execute(anyList())).willReturn(Flux.just("logs"));
    given(commandService.execute(any(Command.class))).willReturn(Flux.just(repositoryUrl));

    final var config = projectService.connect(owner, repositoryUrl).block();
    Assertions.assertThat(config)
        .isNotNull()
        .isEqualTo(GitConfiguration.builder().repositoryUrl(repositoryUrl).build());

    verify(commandService).execute(anyList());

    verify(commandService).execute(commandCaptor.capture());

    final var confCmd = commandCaptor.getValue();
    Assertions.assertThat(confCmd).isEqualTo(Command.builder()
        .path(rootPath.toString())
        .environment(ImmutableMap.of())
        .args(ImmutableList.of("git", "config", "--get", "remote.origin.url"))
        .build());
  }

  @Test
  void shouldDisconnect() {
    final var owner = OwnerTest.USER_OWNER;
    given(ownerToPath.apply(owner)).willReturn(rootPath);
    projectService.disconnect(owner).block();
    Assertions.assertThat(gitPath.toFile().exists()).isFalse();
  }

  @Test
  void shouldIsConnected() {
    final var owner = OwnerTest.USER_OWNER;
    given(ownerToPath.apply(owner)).willReturn(Path.of("nope"));
    Assertions.assertThat(projectService.isConnected(owner)).isFalse();
  }
}