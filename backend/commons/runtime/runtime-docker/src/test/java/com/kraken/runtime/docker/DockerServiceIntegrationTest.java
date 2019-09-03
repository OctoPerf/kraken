package com.kraken.runtime.docker;

import com.kraken.TestConfiguration;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.docker.properties.DockerProperties;
import com.kraken.runtime.entity.TaskTest;
import com.kraken.runtime.entity.TaskType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class}, initializers = {ConfigFileApplicationContextInitializer.class})
@EnableAutoConfiguration
public class DockerServiceIntegrationTest {

  @Autowired
  DockerService dockerService;

  @Autowired
  DockerProperties dockerProperties;

  @Autowired
  CommandService commandService;

//  @Before
//  public void before(){
//    final var up = Command.builder()
//        .path("./testDir")
//        .command(Arrays.asList("docker-compose", "up", "-d"))
//        .environment(ImmutableMap.of())
//        .build();
//    commandService.execute(up).blockLast();
//  }
//
//  @After
//  public void after(){
//    final var down = Command.builder()
//        .path("./testDir")
//        .command(Arrays.asList("docker-compose", "down"))
//        .environment(ImmutableMap.of())
//        .build();
//    commandService.execute(down).blockLast();
//  }

  @Test
  public void shouldListTasks(){
    final var tasks = dockerService.listTasks().collectList().block();
    assertThat(tasks).isNotNull();
    System.out.println(tasks);
    assertThat(tasks.size()).isEqualTo(2);
    final var debugTask = tasks.stream().filter(task -> task.getType() == TaskType.DEBUG).findFirst().orElse(TaskTest.TASK);
    final var runTask = tasks.stream().filter(task -> task.getType() == TaskType.RUN).findFirst().orElse(TaskTest.TASK);
    assertThat(debugTask.getId()).isEqualTo("taskId");
    assertThat(debugTask.getContainers().size()).isEqualTo(2);
    assertThat(runTask.getId()).isEqualTo("taskIdBis");
    assertThat(runTask.getContainers().size()).isEqualTo(1);
  }

  @Test
  public void shouldWatchTasks(){
    final var tasks = dockerService.watchTasks().take(dockerProperties.getWatchTasksDelay().plus(dockerProperties.getWatchTasksDelay()).plusMillis(100)).collectList().block();
    assertThat(tasks).isNotNull();
    assertThat(tasks.size()).isEqualTo(1);
  }
}
