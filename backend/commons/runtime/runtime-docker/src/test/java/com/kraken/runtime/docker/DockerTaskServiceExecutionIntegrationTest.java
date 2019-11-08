package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Log;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.logs.LogsService;
import com.kraken.tools.configuration.properties.ApplicationPropertiesTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, ApplicationPropertiesTestConfiguration.class}, initializers = {ConfigFileApplicationContextInitializer.class})
@EnableAutoConfiguration
public class DockerTaskServiceExecutionIntegrationTest {

  @Autowired
  DockerTaskService taskService;

  @Autowired
  LogsService logsService;

  @Test
  public void shouldExecuteAndCancelStatus() throws InterruptedException {
    final var appId = "appId";
    final var logs = new ArrayList<Log>();
    final var disposable = logsService.listen(appId)
        .subscribeOn(Schedulers.elastic())
        .subscribe(logs::add);

    final var taskId = taskService.execute(appId, TaskType.RECORD, 1, ImmutableMap.of("KRAKEN_IMAGE", "nginx",
        "KRAKEN_GATLING_HAR_PATH_REMOTE", "hars/import.har",
        "KRAKEN_GATLING_SIMULATION_CLASS", "MyClazz",
        "KRAKEN_GATLING_SIMULATION_PACKAGE", "com.test",
        "KRAKEN_DESCRIPTION", "description")).block();

    taskService.watch().filter(tasks -> tasks.size() > 0 && tasks.get(0).getStatus() == ContainerStatus.STARTING).next().block();

    Thread.sleep(5000);
    final var task = taskService.list().next().block();

    assertThat(task).isNotNull();
    assertThat(task.getType()).isEqualTo(TaskType.RECORD);
    assertThat(task.getStatus()).isEqualTo(ContainerStatus.STARTING);
    assertThat(task.getDescription()).isEqualTo("description");
    assertThat(task.getId()).isEqualTo(taskId);
    assertThat(task.getContainers().size()).isEqualTo(2);
    assertThat(task.getContainers().get(0).getId()).startsWith(taskId);
    assertThat(task.getContainers().get(0).getStatus()).isEqualTo(ContainerStatus.STARTING);

    taskService.cancel(appId, task).block();
    Thread.sleep(10000);
    disposable.dispose();
    final var logsString = logs.stream().map(Log::getText).reduce((s, s2) -> s + s2).orElse("");

    System.out.println(logs);

    assertThat(logsString).containsIgnoringCase("Creating");
    assertThat(logsString).containsIgnoringCase("Stopping");
    assertThat(logsString).containsIgnoringCase("Removing");
    assertThat(logsString).containsIgnoringCase("container-two_STARTING ... done");
    assertThat(logsString).containsIgnoringCase("container-one_STARTING ... done");
  }
}
