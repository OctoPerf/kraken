package com.octoperf.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.runtime.command.Command;
import com.octoperf.kraken.runtime.command.CommandService;
import com.octoperf.kraken.runtime.entity.task.FlatContainer;
import com.octoperf.kraken.runtime.entity.task.FlatContainerTest;
import com.octoperf.kraken.security.entity.owner.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DockerContainerFindServiceTest {

  @Mock
  CommandService commandService;
  @Mock
  Function<String, FlatContainer> stringToFlatContainer;
  @Mock
  Function<Owner, List<String>> ownerToFilters;

  DockerContainerFindService service;

  @BeforeEach
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
            "--filter", "label=com.octoperf/taskId=" + container.getTaskId(),
            "--filter", "label=com.octoperf/containerName=" + container.getName(),
            "--format", StringToFlatContainer.FORMAT,
            "--latest"))
        .environment(ImmutableMap.of())
        .build();
    final var logs = Flux.just("logs");
    given(commandService.execute(command)).willReturn(logs);
    given(stringToFlatContainer.apply("logs")).willReturn(container);
    given(ownerToFilters.apply(Owner.PUBLIC)).willReturn(ImmutableList.of());
    final var found = service.find(Owner.PUBLIC, container.getTaskId(), container.getName()).block();
    assertThat(found).isEqualTo(container);
  }
}
