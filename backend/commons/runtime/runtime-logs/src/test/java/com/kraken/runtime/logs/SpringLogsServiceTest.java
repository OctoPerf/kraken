package com.kraken.runtime.logs;

import com.kraken.runtime.entity.Log;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SpringLogsService.class})
public class SpringLogsServiceTest {

  @Autowired
  LogsService service;

  @After
  public void after() throws InterruptedException {
    service.clear();
    Thread.sleep(5000);
  }

  @Test
  public void shouldPushAndListenForLogs() {
    final var applicationId0 = "applicationId0";
    final var applicationId1 = "applicationId1";
    final var applicationId2 = "applicationId2";
    final var log0 = "log0";
    final var log1 = "log1";
    final var log2 = "log2";
    final var log3 = "log3";
    final var logsEmitter0 = Flux.interval(Duration.ofMillis(50)).map(aLong -> log0 + " at " + 50 * (aLong + 1));
    final var logsEmitter1 = Flux.interval(Duration.ofMillis(100)).map(aLong -> log1 + " at " + 100 * (aLong + 1));
    final var logsEmitter2 = Flux.interval(Duration.ofMillis(200)).map(aLong -> log2 + " at " + 200 * (aLong + 1));
    final var logsEmitter3 = Flux.interval(Duration.ofMillis(550)).map(aLong -> log3 + " at " + 550 * (aLong + 1));
    new Thread(() -> {
      service.push(applicationId0, log0, logsEmitter0);
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        e.printStackTrace();
      }
      service.push(applicationId1, log1, logsEmitter1);
      service.push(applicationId2, log2, logsEmitter2);
      service.push(applicationId1, log3, logsEmitter3);
    }).start();

    final var mono0 = service.listen(applicationId0).take(Duration.ofMillis(2200)).collectList();
    final var mono1 = service.listen(applicationId1).take(Duration.ofMillis(2200)).collectList();
    final var mono2 = service.listen(applicationId2).take(Duration.ofMillis(2200)).collectList();
    final var zip = Mono.zip(mono0, mono1, mono2).block();

    assertThat(zip).isNotNull();

    final var app0Logs = zip.getT1();
    assertThat(app0Logs).isNotNull();
    assertThat(app0Logs.size()).isGreaterThan(40);

    final var app1Logs = zip.getT2();
    assertThat(app1Logs).isNotNull();
    assertThat(app1Logs.size()).isGreaterThan(11);
    assertThat(app1Logs.get(0)).isEqualTo(Log.builder().applicationId(applicationId1).id(log1).text("log1 at 100").build());
    assertThat(app1Logs.get(12)).isEqualTo(Log.builder().applicationId(applicationId1).id(log3).text("log3 at 1100").build());

    final var app2Logs = zip.getT2();
    assertThat(app2Logs).isNotNull();
    System.out.println(app1Logs);
    assertThat(app2Logs.size()).isGreaterThan(5);
  }

  @Test
  public void shouldCancelLogs() {
    final var applicationId0 = "applicationId0";
    final var log0 = "log0";
    final var logsEmitter0 = Flux.interval(Duration.ofMillis(100)).map(aLong -> log0 + " at " + 100 * (aLong + 1));
    service.push(applicationId0, log0, logsEmitter0);

    new Thread(() -> {
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        e.printStackTrace();
      }
      service.cancel(log0);
    }).start();

    final var app0Logs = service.listen(applicationId0).take(Duration.ofMillis(2000)).collectList().block();
    assertThat(app0Logs).isNotNull();
    // 1000 ms / 100 => 10 logs max then cancel
    assertThat(app0Logs.size()).isLessThan(11);
  }

  @Test
  public void shouldListenAfterPushingLogs() throws InterruptedException {
    final var applicationId0 = "applicationId0";
    final var log0 = "log0";
    final var logsEmitter0 = Flux.interval(Duration.ofMillis(100)).map(aLong -> log0 + " at " + 100 * (aLong + 1));
    final var app0Logs = new ArrayList<Log>();

    new Thread(() -> {
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        e.printStackTrace();
      }
      final var result = service.listen(applicationId0).take(Duration.ofMillis(1000)).collectList().block();
      assertThat(result).isNotNull();
      app0Logs.addAll(result);
    }).start();

    service.push(applicationId0, log0, logsEmitter0);
    Thread.sleep(3000);
    System.out.println(app0Logs);
    // Wait 1sec, listen for log for 1 sec => 10 logs max
    assertThat(app0Logs.size()).isLessThan(11);
    assertThat(app0Logs.get(0).getText()).isNotEqualTo("log0 at 100");
  }

  @Test
  public void shouldCancelNope() {
    assertThat(service.cancel("nope")).isFalse();
  }

  @Test
  public void shouldConcatLogsCount() {
    final var log0 = "log0";
    final var logsEmitter0 = Flux.interval(Duration.ofMillis(1))
        .map(aLong -> log0 + " at " + 100 * (aLong + 1))
        .take(1000)
        .subscribeOn(Schedulers.elastic());
    final var logs = service.concat(logsEmitter0).collectList().block();

    assertThat(logs).isNotNull();
    assertThat(logs.size()).isEqualTo(2);
  }

  @Test
  public void shouldConcatLogsDuration() {
    final var log0 = "log0";
    final var logsEmitter0 = Flux.interval(Duration.ofMillis(400))
        .map(aLong -> log0 + " at " + 100 * (aLong + 1))
        .take(4)
        .subscribeOn(Schedulers.elastic());
    final var logs = service.concat(logsEmitter0).collectList().block();

    assertThat(logs).isNotNull();
    assertThat(logs.size()).isEqualTo(2);
  }
}

