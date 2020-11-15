package com.octoperf.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.Application;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.runtime.entity.task.FlatContainerTest;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.security.entity.owner.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@Tag("integration")
@EnableAutoConfiguration
public class DockerTaskServiceIntegrationTest {

  @Autowired
  DockerTaskService taskService;

  @Autowired
  CommandService commandService;

  @BeforeEach
  public void before() {
    final var up = Command.builder()
        .path(Paths.get("testDir").toAbsolutePath().toString())
        .args(Arrays.asList("docker-compose", "up", "-d"))
        .environment(ImmutableMap.of())
        .build();
    commandService.execute(up).blockLast();

    final var clean = Command.builder()
        .path(Paths.get("testDir").toAbsolutePath().toString())
        .args(Arrays.asList("/bin/sh", "-c", "docker rm -v $(docker ps -a -q -f status=exited)"))
        .environment(ImmutableMap.of())
        .build();
    commandService.execute(clean).onErrorReturn("").blockLast();
  }

  @AfterEach
  public void after() {
    final var down = Command.builder()
        .path(Paths.get("testDir").toAbsolutePath().toString())
        .args(Arrays.asList("docker-compose", "down"))
        .environment(ImmutableMap.of())
        .build();
    commandService.execute(down).blockLast();
  }

  @Test
  public void shouldListFlatContainers() {
    final var flatContainers = taskService.list(Owner.PUBLIC).collectList().block();
    assertThat(flatContainers).isNotNull();
    System.out.println(flatContainers);
    assertThat(flatContainers.size()).isEqualTo(3);
    final var debugContainers = flatContainers.stream().filter(flatContainer -> flatContainer.getTaskType().equals(TaskType.GATLING_DEBUG)).collect(Collectors.toList());
    final var debugContainer = debugContainers.stream().findFirst().orElse(FlatContainerTest.CONTAINER);
    final var runContainers = flatContainers.stream().filter(flatContainer -> flatContainer.getTaskType().equals(TaskType.GATLING_RUN)).collect(Collectors.toList());
    final var runContainer = runContainers.stream().findFirst().orElse(FlatContainerTest.CONTAINER);
    assertThat(debugContainer.getTaskId()).isEqualTo("taskId");
    assertThat(debugContainer.getDescription()).isEqualTo("Debug Task");
    assertThat(debugContainers.size()).isEqualTo(2);
    assertThat(runContainer.getTaskId()).isEqualTo("taskIdBis");
    assertThat(runContainers.size()).isEqualTo(1);
  }

}
