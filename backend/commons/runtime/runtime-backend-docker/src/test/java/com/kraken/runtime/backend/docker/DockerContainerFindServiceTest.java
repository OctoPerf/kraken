package com.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.entity.log.LogType;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.FlatContainerTest;
import com.kraken.runtime.logs.LogsService;
import com.kraken.security.entity.owner.Owner;
import com.kraken.security.entity.owner.PublicOwner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DockerContainerFindServiceTest {

  @Mock
  CommandService commandService;
  @Mock
  Function<String, FlatContainer> stringToFlatContainer;
  @Mock
  Function<Owner, List<String>> ownerToFilters;

  DockerContainerFindService service;

  @Before
  public void before() {
    this.service = new DockerContainerFindService(commandService, stringToFlatContainer, ownerToFilters);
  }

  @Test
  public void shouldFind() {
    final var container = FlatContainerTest.CONTAINER;
    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "--filter", "label=com.kraken/taskId=" + container.getTaskId(),
            "--filter", "label=com.kraken/containerName=" + container.getName(),
            "--format", StringToFlatContainer.FORMAT,
            "--latest"))
        .environment(ImmutableMap.of())
        .build();
    final var logs = Flux.just("logs");
    given(commandService.execute(command)).willReturn(logs);
    given(stringToFlatContainer.apply("logs")).willReturn(container);
    given(ownerToFilters.apply(PublicOwner.INSTANCE)).willReturn(ImmutableList.of());
    final var found = service.find(PublicOwner.INSTANCE, container.getTaskId(), container.getName()).block();
    assertThat(found).isEqualTo(container);
  }
}
