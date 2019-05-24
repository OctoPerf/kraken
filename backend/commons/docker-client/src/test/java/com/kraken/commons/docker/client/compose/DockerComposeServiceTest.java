package com.kraken.commons.docker.client.compose;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.commons.command.entity.Command;
import com.kraken.commons.command.executor.CommandExecutor;
import com.kraken.test.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DockerComposeServiceTest {

  @Mock
  CommandExecutor executor;
  private DockerComposeService service;
  private Mono<String> commandId;

  @Before
  public void before() {
    commandId = Mono.just("commandId");
    given(executor.execute(any())).willReturn(commandId);
    service = new DockerComposeShellService(executor);
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(DockerComposeShellService.class);
  }

  @Test
  public void shouldUp() {
    assertThat(service.up("app", "/test")).isSameAs(commandId);
    verify(executor).execute(Command.builder()
        .id("")
        .applicationId("app")
        .command(ImmutableList.of("docker-compose", "--no-ansi", "up", "-d", "--no-color"))
        .environment(ImmutableMap.of())
        .path("/test")
        .onCancel(ImmutableList.of())
        .build());
  }

  @Test
  public void shouldPs() {
    assertThat(service.ps("app", "/test")).isSameAs(commandId);
    verify(executor).execute(Command.builder()
        .id("")
        .applicationId("app")
        .command(ImmutableList.of("docker-compose", "--no-ansi", "ps"))
        .environment(ImmutableMap.of())
        .path("/test")
        .onCancel(ImmutableList.of())
        .build());
  }

  @Test
  public void shouldLogs() {
    assertThat(service.logs("app", "/test")).isSameAs(commandId);
    verify(executor).execute(Command.builder()
        .id("")
        .applicationId("app")
        .command(ImmutableList.of("docker-compose", "--no-ansi", "logs", "--no-color"))
        .environment(ImmutableMap.of())
        .path("/test")
        .onCancel(ImmutableList.of())
        .build());
  }

  @Test
  public void shouldDown() {
    assertThat(service.down("app", "/test")).isSameAs(commandId);
    verify(executor).execute(Command.builder()
        .id("")
        .applicationId("app")
        .command(ImmutableList.of("docker-compose", "--no-ansi", "down"))
        .environment(ImmutableMap.of())
        .path("/test")
        .onCancel(ImmutableList.of())
        .build());
  }

}
