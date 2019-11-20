package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.ExecutionContext;
import com.kraken.runtime.entity.Log;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.logs.LogsService;
import com.kraken.tools.configuration.properties.ApplicationPropertiesTestConfiguration;
import com.kraken.tools.environment.KrakenEnvironmentKeys;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
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

    final var context = ExecutionContext.builder()
        .taskType(TaskType.RECORD)
        .taskId("execution-integration-test")
        .applicationId(appId)
        .description("description")
        .environment( ImmutableMap.of("KRAKEN_IMAGE", "nginx",
            KRAKEN_GATLING_HAR_PATH_REMOTE, "hars/import.har",
            KRAKEN_GATLING_SIMULATION_CLASS, "MyClazz",
            KRAKEN_GATLING_SIMULATION_PACKAGE, "com.test"))
        .hosts(ImmutableMap.of())
        .build();

    taskService.execute(context).block();

    Thread.sleep(5000);
    final var flatContainers = taskService.list().collectList().block();
    assertThat(flatContainers).isNotNull();
    assertThat(flatContainers.size()).isEqualTo(2);

    final var flatContainer = flatContainers.get(0);

    assertThat(flatContainer).isNotNull();
    assertThat(flatContainer.getTaskId()).isEqualTo(context.getTaskId());
    assertThat(flatContainer.getTaskType()).isEqualTo(TaskType.RECORD);
    assertThat(flatContainer.getStatus()).isEqualTo(ContainerStatus.STARTING);
    assertThat(flatContainer.getDescription()).isEqualTo("description");

    taskService.cancel(appId, flatContainer.getTaskId(), flatContainer.getTaskType()).block();
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
