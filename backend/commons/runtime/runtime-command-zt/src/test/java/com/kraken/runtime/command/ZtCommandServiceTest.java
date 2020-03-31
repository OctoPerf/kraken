package com.kraken.runtime.command;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.tools.environment.KrakenEnvironmentKeys;
import com.kraken.tools.reactor.utils.ReactorUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_VERSION;
import static com.kraken.tools.reactor.utils.ReactorUtils.waitFor;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {StringCleaner.class, ZtCommandService.class})
public class ZtCommandServiceTest {

  static final int SLEEP = 2000;

  @Autowired
  Function<String, String> stringCleaner;

  @Autowired
  CommandService service;

  @Test
  public void shouldEchoEnvData() {
    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("/bin/sh", "-c", "echo $KRAKEN_VERSION"))
        .environment(ImmutableMap.of(KRAKEN_VERSION, "BAR"))
        .build();
    final var result = service.execute(command).take(1).collectList().block();
    assertThat(result).isNotNull();
    assertThat(result.size()).isEqualTo(1);
    assertThat(result.get(0)).startsWith("BAR");
  }

  @Test
  public void shouldPrintEnv() {
    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("/bin/sh", "-c", "printenv"))
        .environment(ImmutableMap.of(KRAKEN_VERSION, "BAR"))
        .build();
    final var result = service.execute(command).collectList().block();
    assertThat(result).isNotNull();
    assertThat(result.size()).isGreaterThan(1);
    System.out.println(String.join("\n", result));
  }

  @Test
  public void shouldCancelSleepSimple() throws InterruptedException {
    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("/bin/sh", "-c", "sleep 5 && echo run"))
        .environment(ImmutableMap.of())
        .build();
    final var logs = new ArrayList<String>();
    final var subscription = service.execute(command)
        .subscribeOn(Schedulers.elastic()) // Prevents the .subscribe() from blocking the test execution
        .subscribe(logs::add);
    Thread.sleep(SLEEP);
    subscription.dispose();
    System.out.println(logs);
    assertThat(logs.size()).isEqualTo(0);
  }

  @Test
  public void shouldCommandFail() {
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
  public void shouldCommandExitStatus() {
    final var command = Command.builder()
        .path(".")
        .command(ImmutableList.of("cat", "doesnotexist.txt"))
        .environment(ImmutableMap.of())
        .build();
    StepVerifier.create(service.execute(command))
        .expectNext("cat: doesnotexist.txt: Aucun fichier ou dossier de ce type")
        .expectError(IllegalArgumentException.class)
        .verify();
  }

  @Test
  public void shouldRunDockerCommands() {
    final var up = Command.builder()
        .path("./testDir")
        .command(Arrays.asList("docker-compose", "up", "-d"))
        .environment(ImmutableMap.of())
        .build();
    final var ps = Command.builder()
        .path("./testDir")
        .command(Arrays.asList("docker",
            "ps",
            "--filter", "label=com.kraken/id",
            "--filter", "status=running",
            "--format", "\"{{.ID}};{{.Names}};{{.Label \"com.kraken/id\"}}\""))
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

  @Test
  public void shouldAwait() throws InterruptedException {
    final var path = Paths.get("testDir/echo").toAbsolutePath().toString();
    final var up = Command.builder()
        .path(path)
        .command(Arrays.asList("docker-compose", "up"))
        .environment(ImmutableMap.of())
        .build();
    final var down = Command.builder()
        .path(path)
        .command(Arrays.asList("docker-compose", "down"))
        .environment(ImmutableMap.of())
        .build();

    final var logsBuilder = ImmutableList.<String>builder();

    waitFor(service.execute(up).doOnNext(logsBuilder::add), Mono.just("Done !"), Duration.ofSeconds(3));

    service.execute(down).subscribe(logsBuilder::add);

    final var logs = String.join("\n", logsBuilder.build());

    System.out.println(logs);

    assertThat(logs).contains("Creating the-dolphin");
    assertThat(logs).contains("the-dolphin    | Kraken echo!");
    assertThat(logs).contains("Stopping the-dolphin ... done");
    assertThat(logs).contains("Removing the-dolphin ... done");
  }

  @Test
  public void shouldCat() {
    final var path = Paths.get("testDir/echo").toAbsolutePath().toString();
    final var cat = Command.builder()
        .path(path)
        .command(Arrays.asList("cat", "docker-compose.yml"))
        .environment(ImmutableMap.of())
        .build();

    final var logs = String.join("\n", Optional.ofNullable(service.execute(cat).collectList().block()).orElse(Collections.emptyList()));

    System.out.println(logs);

    assertThat(logs).contains("kraken");
  }
}
