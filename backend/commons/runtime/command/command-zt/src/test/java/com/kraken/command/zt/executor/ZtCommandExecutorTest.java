package com.kraken.command.zt.executor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.command.zt.TestConfiguration;
import com.kraken.command.entity.Command;
import com.kraken.command.entity.CommandLog;
import com.kraken.command.entity.CommandLogStatus;
import com.kraken.command.zt.logs.LogsQueue;
import com.kraken.tools.configuration.properties.ApplicationProperties;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.Disposable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class ZtCommandExecutorTest {

  static final int SLEEP = 3000;

  @Autowired
  Function<String, String> stringCleaner;

  @Autowired
  Map<String, Disposable> subscriptionsMap;

  @Autowired
  ZtCommandExecutor executor;

  @Autowired
  ApplicationProperties applicationProperties;

  @After
  public void after() {
    executor.clear();
  }

  @Test
  public void shouldEchoHostData() throws InterruptedException {
    final var list = ImmutableList.<CommandLog>builder();
    final var listen = executor.listen("app")
        .subscribe(list::add);
    final var command = Command.builder()
        .id("")
        .applicationId("app")
        .path(".")
        .command(Arrays.asList("/bin/sh", "-c", "echo $KRAKEN_HOST_DATA"))
        .environment(ImmutableMap.of())
        .onCancel(Collections.emptyList())
        .build();
    final var execute = executor.execute(command).subscribe();
    Thread.sleep(SLEEP);
    execute.dispose();
    listen.dispose();
    final var result = list.build();
    assertThat(result.size()).isEqualTo(3);
    assertThat(result.get(0).getStatus()).isEqualTo(CommandLogStatus.INITIALIZED);
    assertThat(result.get(1).getText()).isEqualTo("/home/ubuntu\r\n");
    assertThat(result.get(2).getStatus()).isEqualTo(CommandLogStatus.CLOSED);
  }


  @Test
  public void shouldEchoTest() throws InterruptedException {
    final var list = ImmutableList.<CommandLog>builder();
    final var listen = executor.listen("app")
        .subscribe(list::add);
    final var command = Command.builder()
        .id("")
        .applicationId("app")
        .path(".")
        .command(Arrays.asList("/bin/sh", "-c", "echo $NAME"))
        .environment(ImmutableMap.of("NAME", "test"))
        .onCancel(Collections.emptyList())
        .build();
    final var execute = executor.execute(command).subscribe();
    Thread.sleep(SLEEP);
    execute.dispose();
    listen.dispose();
    final var result = list.build();
    assertThat(result.size()).isEqualTo(3);
    assertThat(result.get(0).getStatus()).isEqualTo(CommandLogStatus.INITIALIZED);
    assertThat(result.get(1).getText()).isEqualTo("test\r\n");
    assertThat(result.get(2).getStatus()).isEqualTo(CommandLogStatus.CLOSED);
  }

  @Test
  public void shouldCancelSleep() throws InterruptedException {
    final var list = ImmutableList.<CommandLog>builder();
    final var listen = executor.listen("app")
        .subscribe(list::add);
    final var command = Command.builder()
        .id("")
        .applicationId("app")
        .path(".")
        .command(Arrays.asList("/bin/sh", "-c", "sleep 10 && echo run"))
        .environment(ImmutableMap.of())
        .onCancel(Arrays.asList("/bin/sh", "-c", "echo cancel"))
        .build();
    final var id = executor.execute(command).block();
    Thread.sleep(SLEEP);
    executor.cancel(id);
    Thread.sleep(SLEEP);
    listen.dispose();
    final var result = list.build();
    System.out.println(result);
    assertThat(result.size()).isEqualTo(4);
    assertThat(result.get(0).getStatus()).isEqualTo(CommandLogStatus.INITIALIZED);
    assertThat(result.get(1).getStatus()).isEqualTo(CommandLogStatus.CANCELLING);
    assertThat(result.get(2).getStatus()).isEqualTo(CommandLogStatus.CANCELLING);
    assertThat(result.get(2).getText()).isEqualTo("cancel\r\n");
    assertThat(result.get(3).getStatus()).isEqualTo(CommandLogStatus.CLOSED);
  }

  @Test
  public void shouldCancelSleepSimple() throws InterruptedException {
    final var list = ImmutableList.<CommandLog>builder();
    final var listen = executor.listen("app")
        .subscribe(list::add);
    final var command = Command.builder()
        .id("")
        .applicationId("app")
        .path(".")
        .command(Arrays.asList("/bin/sh", "-c", "sleep 10 && echo run"))
        .environment(ImmutableMap.of())
        .onCancel(ImmutableList.of())
        .build();
    final var id = executor.execute(command).block();
    Thread.sleep(SLEEP);
    executor.cancel(id);
    Thread.sleep(SLEEP);
    listen.dispose();
    final var result = list.build();
    System.out.println(result);
    assertThat(result.size()).isEqualTo(2);
    assertThat(result.get(0).getStatus()).isEqualTo(CommandLogStatus.INITIALIZED);
    assertThat(result.get(1).getStatus()).isEqualTo(CommandLogStatus.CLOSED);
  }

  @Test
  public void shouldCommandFail() throws InterruptedException {
    final var list = ImmutableList.<CommandLog>builder();
    final var listen = executor.listen("app")
        .subscribe(list::add);
    final var command = Command.builder()
        .id("")
        .applicationId("app")
        .path(".")
        .command(Arrays.asList("ca va fail !"))
        .environment(ImmutableMap.of())
        .onCancel(Collections.emptyList())
        .build();
    final var execute = executor.execute(command).subscribe();
    Thread.sleep(10000);
    execute.dispose();
    listen.dispose();
    final var result = list.build();
    System.out.println(result);
    assertThat(result.size()).isEqualTo(3);
    assertThat(result.get(0).getStatus()).isEqualTo(CommandLogStatus.INITIALIZED);
    assertThat(result.get(1).getText()).startsWith("An exception occurred while executing the command");
    assertThat(result.get(2).getStatus()).isEqualTo(CommandLogStatus.CLOSED);
  }

  @Test
  public void shouldCancelFail() {
    assertThat(executor.cancel("unknown")).isFalse();
  }

  @Test
  public void shouldOnErrorInterrupt() {
    final var queue = BDDMockito.mock(LogsQueue.class);
    final var executor = new ZtCommandExecutor(stringCleaner, subscriptionsMap, queue, applicationProperties);
    final var command = Command.builder()
        .id("id")
        .applicationId("app")
        .path(".")
        .command(Arrays.asList("java", "--version"))
        .environment(ImmutableMap.of())
        .onCancel(Collections.emptyList())
        .build();
    executor.onError(command, new InterruptedException("FAIL"));
    BDDMockito.verify(queue).publish(command, CommandLogStatus.CLOSED, "");
  }

  @Test
  public void shouldListenFail() {
    final var queue = new ErrorLogsQueue();
    final var executor = new ZtCommandExecutor(stringCleaner, subscriptionsMap, queue, applicationProperties);
    executor.listen("app").subscribe();
    assertThat(queue.removedCount.get()).isEqualTo(1);
  }

  @Test
  public void shouldRunDockerCommands() throws InterruptedException {
    final var listen = executor.listen("app")
        .subscribe(log -> System.out.println(log.getText()));
    final var command = Command.builder()
        .id("")
        .applicationId("app")
        .path(".")
        .command(Arrays.asList("docker", "pull", "nginx:latest"))
        .environment(ImmutableMap.of())
        .onCancel(Collections.emptyList())
        .build();
    final var execute = executor.execute(command).subscribe();
    Thread.sleep(10000);
    execute.dispose();
    listen.dispose();
  }
}
