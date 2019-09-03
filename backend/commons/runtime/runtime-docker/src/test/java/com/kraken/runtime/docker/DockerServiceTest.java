package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.TestConfiguration;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.entity.TaskType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class}, initializers = {ConfigFileApplicationContextInitializer.class})
@EnableAutoConfiguration
public class DockerServiceTest {

  @Autowired
  DockerService dockerService;

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
    final var tasks = dockerService.list().collectList().block();
    assertThat(tasks).isNotNull();
    System.out.println(tasks);
    assertThat(tasks.size()).isEqualTo(2);
    assertThat(tasks.get(0).getType()).isEqualTo(TaskType.DEBUG);
    assertThat(tasks.get(0).getContainers().size()).isEqualTo(2);
    assertThat(tasks.get(1).getType()).isEqualTo(TaskType.RUN);
    assertThat(tasks.get(1).getContainers().size()).isEqualTo(1);
  }
}
