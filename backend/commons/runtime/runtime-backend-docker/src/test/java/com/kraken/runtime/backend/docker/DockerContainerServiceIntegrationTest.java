package com.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.Application;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.entity.log.Log;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.logs.LogsService;
import com.kraken.security.entity.owner.Owner;
import com.kraken.security.entity.owner.UserOwner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;

import static com.kraken.security.entity.token.KrakenRole.USER;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DockerContainerServiceIntegrationTest {

  @Autowired
  DockerContainerService containerService;

  @Autowired
  LogsService logsService;

  @Autowired
  CommandService commandService;

  @Before
  public void before() {
    final var up = Command.builder()
        .path("./testDir")
        .command(Arrays.asList("docker-compose", "up", "-d"))
        .environment(ImmutableMap.of())
        .build();
    commandService.execute(up).blockLast();
  }

  @After
  public void after() {
    final var down = Command.builder()
        .path("./testDir")
        .command(Arrays.asList("docker-compose", "down"))
        .environment(ImmutableMap.of())
        .build();
    commandService.execute(down).blockLast();
  }

  @Test
  public void shouldDisplayLogs() throws InterruptedException {
    final var appId = "app";
    final var userId = "user";
    final var owner = UserOwner.builder().applicationId(appId).userId(userId).roles(ImmutableList.of(USER)).build();
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
    final var owner = UserOwner.builder().applicationId(appId).userId(userId).roles(ImmutableList.of(USER)).build();
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
