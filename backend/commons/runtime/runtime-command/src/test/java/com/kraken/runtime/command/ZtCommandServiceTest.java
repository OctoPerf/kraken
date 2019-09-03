package com.kraken.runtime.command;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {StringCleaner.class, ZtCommandService.class})
public class ZtCommandServiceTest {

  static final int SLEEP = 3000;

  @Autowired
  Function<String, String> stringCleaner;

  @Autowired
  CommandService service;

  @Test
  public void shouldEchoEnvData() {
    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("/bin/sh", "-c", "echo $FOO"))
        .environment(ImmutableMap.of("FOO", "BAR"))
        .build();
    final var result = service.execute(command).take(1).collectList().block();
    assertThat(result).isNotNull();
    assertThat(result.size()).isEqualTo(1);
    assertThat(result.get(0)).startsWith("BAR");
  }

  @Test
  public void shouldCancelSleepSimple() throws InterruptedException {
    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("/bin/sh", "-c", "sleep 5 && echo run"))
        .environment(ImmutableMap.of())
        .build();
    final var logs = new ArrayList<String>();
    final var subscription = service.execute(command).subscribe(logs::add);
    Thread.sleep(SLEEP);
    subscription.dispose();
    System.out.println(logs);
    assertThat(logs.size()).isEqualTo(0);
  }

  @Test
  public void shouldCommandFail() throws InterruptedException {
    final var command = Command.builder()
        .path(".")
        .command(Collections.singletonList("ca va fail !"))
        .environment(ImmutableMap.of())
        .build();
    StepVerifier.create(service.execute(command))
        .expectError()
        .verify();
  }

  @Test
  public void shouldRunDockerCommands() throws InterruptedException {
    final var up = Command.builder()
        .path("./testDir")
        .command(Arrays.asList("docker-compose", "up", "-d"))
        .environment(ImmutableMap.of())
        .build();
    final var ps = Command.builder()
        .path("./testDir")
        .command(Arrays.asList("docker",
            "ps",
            "--filter", "label=com.kraken.id",
            "--filter", "status=running",
            "--format", "\"{{.ID}};{{.Names}};{{.Label \"com.kraken.id\"}}\""))
        .environment(ImmutableMap.of())
        .build();
    final var rename = Command.builder()
        .path("./testDir")
        .command(Arrays.asList("docker",
            "rename",
            "test-nginx-STARTING",
            "test-nginx-READY"))
        .environment(ImmutableMap.of())
        .build();
    final var down = Command.builder()
        .path("./testDir")
        .command(Arrays.asList("docker-compose", "down"))
        .environment(ImmutableMap.of())
        .build();
    final var upResult = service.execute(up).collectList().block();
    assertThat(upResult).isNotNull();
    final var upString = String.join("", upResult);
    System.out.println(upString);
    assertThat(upString).contains("Creating network \"commons-test\"");
    assertThat(upString).contains("Creating test-nginx-STARTING");

    final var psResult = service.execute(ps).collectList().block();
    assertThat(psResult).isNotNull();
    final var psString = String.join("", psResult);
    System.out.println(psString);
    assertThat(psString).contains("test-nginx-STARTING");
    assertThat(psString).contains("some-kraken-id");

    service.execute(rename).blockLast();

    final var ps2Result = service.execute(ps).collectList().block();
    assertThat(ps2Result).isNotNull();
    final var ps2String = String.join("", ps2Result);
    System.out.println(ps2String);
    assertThat(ps2String).contains("test-nginx-READY");
    assertThat(ps2String).contains("some-kraken-id");

    final var downResult = service.execute(down).collectList().block();
    assertThat(downResult).isNotNull();
    final var downString = String.join("", downResult);
    System.out.println(downResult);
    assertThat(downString).contains("Stopping");

  }
}
