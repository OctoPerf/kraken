package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.entity.*;
import com.kraken.runtime.logs.LogsService;
import com.kraken.tools.configuration.properties.ApplicationPropertiesTestConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, ApplicationPropertiesTestConfiguration.class}, initializers = {ConfigFileApplicationContextInitializer.class})
@EnableAutoConfiguration
public class DockerContainerServiceIntegrationTest {

  @Autowired
  DockerContainerService containerService;

  @Autowired
  DockerTaskService taskService;

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
    final var appId = "appId";
    final var taskId = "taskId";
    final var hostId = "local";
    final var containerId = "containerThreeId";
    final var logs = new ArrayList<Log>();
    final var disposable = logsService.listen(appId)
        .subscribeOn(Schedulers.elastic())
        .subscribe(logs::add);

    final var id = containerService.attachLogs(appId, taskId, hostId, containerId).block();
    Thread.sleep(5000);
    containerService.detachLogs(id).block();
    disposable.dispose();

    System.out.println(logs);
    final var first = logs.get(0);
    assertThat(first.getId()).isEqualTo("taskId-local-containerThreeId");
    assertThat(first.getApplicationId()).isEqualTo(appId);
    assertThat(first.getText()).contains("Kraken echo!");
  }

  @Test
  public void shouldSetStatus() {
    final var taskId = "taskId";
    final var hostId = "local";
    final var containerId = "containerOneId";
    containerService.setStatus(taskId, hostId, containerId, ContainerStatus.RUNNING).subscribeOn(Schedulers.elastic()).then().block();

    final var flatContainers = taskService.list().collectList().block();
    assertThat(flatContainers).isNotNull();
    System.out.println(flatContainers);
    assertThat(flatContainers.size()).isEqualTo(3);
    final var debugFlatContainer = flatContainers.stream().filter(flatContainer -> flatContainer.getContainerId().equals(containerId)).findFirst();
    assertThat(debugFlatContainer.isPresent()).isTrue();
    assertThat(debugFlatContainer.get().getStatus()).isEqualTo(ContainerStatus.RUNNING);
  }
}
