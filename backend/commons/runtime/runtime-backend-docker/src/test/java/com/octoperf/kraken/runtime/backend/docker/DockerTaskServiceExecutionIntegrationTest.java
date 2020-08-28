package com.octoperf.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.Application;
import com.octoperf.kraken.runtime.command.Command;
import com.octoperf.kraken.runtime.command.CommandService;
import com.octoperf.kraken.runtime.context.entity.CancelContext;
import com.octoperf.kraken.runtime.context.entity.ExecutionContext;
import com.octoperf.kraken.runtime.entity.log.Log;
import com.octoperf.kraken.runtime.entity.task.ContainerStatus;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.runtime.logs.LogsService;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerType;
import com.octoperf.kraken.tests.utils.ResourceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static com.octoperf.kraken.security.entity.token.KrakenRole.USER;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@Tag("integration")
@SuppressWarnings("squid:S2925")
public class DockerTaskServiceExecutionIntegrationTest {

  @Autowired
  DockerTaskService taskService;
  @Autowired
  LogsService logsService;
  @Autowired
  CommandService commandService;

  @BeforeEach
  public void before() {
    final var clean = Command.builder()
        .path(Paths.get("testDir").toAbsolutePath().toString())
        .commands(Arrays.asList("/bin/sh", "-c", "docker rm -v $(docker ps -a -q -f status=exited)"))
        .environment(ImmutableMap.of())
        .build();
    commandService.execute(clean).onErrorReturn("").blockLast();
  }

  @Test
  public void shouldExecuteAndCancelStatus() throws InterruptedException, IOException {
    final var taskId = "taskId";
    final var appId = "test";
    final var projectId = "project";
    final var userId = "user";
    final var owner = Owner.builder().applicationId(appId).projectId(projectId).userId(userId).roles(ImmutableList.of(USER)).type(OwnerType.USER).build();
    final var taskType = TaskType.GATLING_RECORD;
    final var template = ResourceUtils.getResourceContent("docker-compose.yml");
    final var logs = new ArrayList<Log>();
    final var disposable = logsService.listen(owner)
        .subscribeOn(Schedulers.elastic())
        .subscribe(logs::add);

    final var context = ExecutionContext.builder()
        .taskType(taskType)
        .taskId(taskId)
        .owner(owner)
        .templates(ImmutableMap.of("local", template))
        .description("description")
        .build();

    taskService.execute(context).block();

    Thread.sleep(5000);
    final var flatContainers = taskService.list(owner).collectList().block();
    assertThat(flatContainers).isNotNull();
    assertThat(flatContainers.size()).isEqualTo(2);

    final var flatContainer = flatContainers.get(0);

    assertThat(flatContainer).isNotNull();
    assertThat(flatContainer.getTaskId()).isEqualTo(context.getTaskId());
    assertThat(flatContainer.getTaskType()).isEqualTo(context.getTaskType());
    assertThat(flatContainer.getStatus()).isEqualTo(ContainerStatus.STARTING);
    assertThat(flatContainer.getDescription()).isEqualTo("description");

    taskService.cancel(CancelContext.builder().owner(owner).taskId(taskId).taskType(taskType).build()).block();
    Thread.sleep(10000);
    disposable.dispose();
    final var logsString = logs.stream().map(Log::getText).reduce((s, s2) -> s + s2).orElse("");

    System.out.println(logs);

    assertThat(logsString).containsIgnoringCase("Creating");
    assertThat(logsString).containsIgnoringCase("container-two_STARTING ... done");
    assertThat(logsString).containsIgnoringCase("container-one_STARTING ... done");
  }
}
