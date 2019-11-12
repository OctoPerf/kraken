package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Log;
import com.kraken.runtime.entity.TaskTest;
import com.kraken.runtime.entity.TaskType;
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

    containerService.attachLogs(appId, taskId, hostId, containerId).block();
    Thread.sleep(5000);
    containerService.detachLogs(taskId, hostId, containerId).block();
    disposable.dispose();

    System.out.println(logs);
    final var first = logs.get(0);
    assertThat(first.getId()).isEqualTo(containerId);
    assertThat(first.getApplicationId()).isEqualTo(appId);
    assertThat(first.getText()).contains("Kraken echo!");
  }

  @Test
  public void shouldSetStatus() {
    final var taskId = "taskId";
    final var hostId = "local";
    final var containerId = "containerOneId";
    final var container = containerService.setStatus(taskId, hostId, containerId, ContainerStatus.RUNNING).subscribeOn(Schedulers.elastic()).block();

    System.out.println(container);
    assertThat(container).isNotNull();
    assertThat(container.getId()).isEqualTo(containerId);

    final var tasks = taskService.list().collectList().block();
    assertThat(tasks).isNotNull();
    System.out.println(tasks);
    assertThat(tasks.size()).isEqualTo(2);
    final var debugTask = tasks.stream().filter(task -> task.getType() == TaskType.DEBUG).findFirst().orElse(TaskTest.TASK);
    assertThat(debugTask.getStatus()).isEqualTo(ContainerStatus.RUNNING);
  }
}
