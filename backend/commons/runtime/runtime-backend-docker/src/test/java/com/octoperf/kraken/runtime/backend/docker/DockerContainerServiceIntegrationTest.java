package com.octoperf.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.Application;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.runtime.entity.log.Log;
import com.octoperf.kraken.runtime.entity.task.ContainerStatus;
import com.octoperf.kraken.runtime.entity.task.FlatContainer;
import com.octoperf.kraken.runtime.logs.TaskLogsService;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;

import static com.octoperf.kraken.security.entity.token.KrakenRole.USER;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@Tag("integration")
@SuppressWarnings("squid:S2925")
public class DockerContainerServiceIntegrationTest {

  @Autowired
  DockerContainerService containerService;

  @Autowired
  TaskLogsService logsService;

  @Autowired
  CommandService commandService;

  @BeforeEach
  public void before() {
    final var up = Command.builder()
        .path("./testDir")
        .args(Arrays.asList("docker-compose", "up", "-d"))
        .environment(ImmutableMap.of())
        .build();
    commandService.execute(up).blockLast();
  }

  @AfterEach
  public void after() {
    final var down = Command.builder()
        .path("./testDir")
        .args(Arrays.asList("docker-compose", "down"))
        .environment(ImmutableMap.of())
        .build();
    commandService.execute(down).blockLast();
  }

  @Test
  public void shouldDisplayLogs() throws InterruptedException {
    final var appId = "app";
    final var userId = "user";
    final var projectId = "project";
    final var owner = Owner.builder().applicationId(appId).projectId(projectId).userId(userId).roles(ImmutableList.of(USER)).type(OwnerType.USER).build();
    final var taskId = "taskIdBis";
    final var containerName = "containerThreeId";
    final var logs = new ArrayList<Log>();
    final var disposable = logsService.listen(owner)
        .subscribeOn(Schedulers.elastic())
        .subscribe(logs::add);

    final var container = this.getContainer(owner, taskId, containerName);

    final var id = containerService.attachLogs(owner, taskId, container.getId(), containerName).block();
    Thread.sleep(5000);
    containerService.detachLogs(owner, id).block();
    disposable.dispose();

    System.out.println(logs);
    final var first = logs.get(0);
    assertThat(first.getId()).isEqualTo("taskIdBis-" + container.getId() + "-containerThreeId");
    assertThat(first.getOwner()).isEqualTo(owner);
    assertThat(first.getText()).contains("Kraken echo!");
  }

  @Test
  public void shouldSetStatus() {
    final var appId = "app";
    final var userId = "user";
    final var projectId = "project";
    final var owner = Owner.builder().applicationId(appId).projectId(projectId).userId(userId).roles(ImmutableList.of(USER)).type(OwnerType.USER).build();
    final var taskId = "taskId";
    final var containerName = "containerOneId";

    final var container = this.getContainer(owner, taskId, containerName);

    containerService.setStatus(owner, taskId, container.getId(), containerName, ContainerStatus.RUNNING).subscribeOn(Schedulers.elastic()).then().block();

    final var debugFlatContainer = this.getContainer(owner, taskId, containerName);

    assertThat(debugFlatContainer.getStatus()).isEqualTo(ContainerStatus.RUNNING);
  }

  private FlatContainer getContainer(final Owner owner, final String taskId, final String containerName) {
    final var container = containerService.find(owner, taskId, containerName).block();
    assertThat(container).isNotNull();
    return container;
  }
}
