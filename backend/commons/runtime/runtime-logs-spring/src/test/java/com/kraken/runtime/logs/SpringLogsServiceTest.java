package com.kraken.runtime.logs;

import com.kraken.runtime.entity.log.Log;
import com.kraken.runtime.entity.log.LogStatus;
import com.kraken.runtime.entity.log.LogType;
import com.kraken.security.entity.owner.ApplicationOwner;
import com.kraken.security.entity.owner.PublicOwner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringLogsServiceTest {

  SpringLogsService service;

  @Before
  public void before() {
    service = new SpringLogsService();
    service.init();
  }

  @After
  public void after() throws InterruptedException {
    service.clear();
    Thread.sleep(5000);
  }

  @Test
  public void shouldPushAndListenForLogs() {
    final var applicationId0 = ApplicationOwner.builder().applicationId("applicationId0").build();
    final var applicationId1 = ApplicationOwner.builder().applicationId("applicationId1").build();
    final var applicationId2 = ApplicationOwner.builder().applicationId("applicationId2").build();
    final var log0 = "log0";
    final var log1 = "log1";
    final var log2 = "log2";
    final var log3 = "log3";
    final var logsEmitter0 = Flux.interval(Duration.ofMillis(50)).map(aLong -> log0 + " at " + 50 * (aLong + 1));
    final var logsEmitter1 = Flux.interval(Duration.ofMillis(100)).map(aLong -> log1 + " at " + 100 * (aLong + 1));
    final var logsEmitter2 = Flux.interval(Duration.ofMillis(200)).map(aLong -> log2 + " at " + 200 * (aLong + 1));
    final var logsEmitter3 = Flux.interval(Duration.ofMillis(550)).map(aLong -> log3 + " at " + 550 * (aLong + 1));
    new Thread(() -> {
      service.push(applicationId0, log0, LogType.CONTAINER, logsEmitter0);
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        e.printStackTrace();
      }
      service.push(applicationId1, log1, LogType.CONTAINER, logsEmitter1);
      service.push(applicationId2, log2, LogType.CONTAINER, logsEmitter2);
      service.push(applicationId1, log3, LogType.CONTAINER, logsEmitter3);
    }).start();

    final var mono0 = service.listen(applicationId0).take(Duration.ofMillis(2200)).collectList();
    final var mono1 = service.listen(applicationId1).take(Duration.ofMillis(2200)).collectList();
    final var mono2 = service.listen(applicationId2).take(Duration.ofMillis(2200)).collectList();
    final var zip = Mono.zip(mono0, mono1, mono2).block();

    assertThat(zip).isNotNull();

    final var app0Logs = zip.getT1();
    assertThat(app0Logs).isNotNull();
    assertThat(app0Logs.size()).isEqualTo(2);

    final var app1Logs = zip.getT2();
    assertThat(app1Logs).isNotNull();
    assertThat(app1Logs.size()).isEqualTo(2);

    final var app2Logs = zip.getT2();
    assertThat(app2Logs).isNotNull();
    System.out.println(app2Logs);
    assertThat(app2Logs.size()).isEqualTo(2);
  }

  @Test
  public void shouldCancelLogs() throws InterruptedException {
    final var applicationId0 = ApplicationOwner.builder().applicationId("applicationId0").build();
    final var log0 = "log0";
    final var logsEmitter0 = Flux.interval(Duration.ofMillis(100)).map(aLong -> log0 + " at " + 100 * (aLong + 1));
    service.push(applicationId0, log0, LogType.CONTAINER, logsEmitter0);
    final var app0Logs = new ArrayList<Log>();
    final var disposable = service.listen(applicationId0).subscribeOn(Schedulers.elastic()).subscribe(app0Logs::add);
    Thread.sleep(3000);
    service.dispose(applicationId0, log0, LogType.CONTAINER);
    Thread.sleep(1000);
    disposable.dispose();
    System.out.println(app0Logs);
    assertThat(app0Logs).isNotNull();
    assertThat(app0Logs.size()).isLessThan(5);
    assertThat(app0Logs.get(app0Logs.size() - 1).getStatus()).isEqualTo(LogStatus.CLOSED);
  }

  @Test
  public void shouldListenAfterPushingLogs() throws InterruptedException {
    final var applicationId0 = ApplicationOwner.builder().applicationId("applicationId0").build();
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

    service.push(applicationId0, log0, LogType.CONTAINER, logsEmitter0);
    Thread.sleep(3000);
    System.out.println(app0Logs);
    // Wait 1sec, listen for log for 1 sec => 10 logs max
    assertThat(app0Logs.size()).isLessThan(11);
    assertThat(app0Logs.get(0).getText()).isNotEqualTo("log0 at 100");
  }

  @Test
  public void shouldCancelNope() {
    assertThat(service.dispose(PublicOwner.INSTANCE,"nope", LogType.CONTAINER)).isFalse();
  }


  @Test
  public void shouldAdd() {
    final var appId = ApplicationOwner.builder().applicationId("appId").build();
    final var id = "id";
    final var log = Log.builder().owner(appId).id(id).text("text").type(LogType.TASK).status(LogStatus.RUNNING).build();
    new Thread(() -> {
      try {
        Thread.sleep(100);
      } catch (Exception e) {
        e.printStackTrace();
      }
      service.add(log);
    }).start();
    final var result = service.listen(appId).take(1).blockFirst();
    assertThat(result).isSameAs(log);
  }

  @Test
  public void shouldNotAdd() {
    final var appId = ApplicationOwner.builder().applicationId("appId").build();
    final var id = "id";
    final var log = Log.builder().owner(appId).id(id).text("text").status(LogStatus.RUNNING).type(LogType.TASK).build();
    // Won't do anything
    service.add(log);
    final var result = service.listen(appId).take(Duration.ofMillis(100)).blockFirst();
    assertThat(result).isNull();
  }
}

