package com.kraken.commons.command.logs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.commons.command.entity.Command;
import com.kraken.commons.command.entity.CommandLog;
import com.kraken.commons.command.entity.CommandLogStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Collections;

import static com.kraken.commons.command.entity.CommandTest.SHELL_COMMAND;
import static org.assertj.core.api.Assertions.assertThat;

public class ConcurrentLogsQueueTest {

  private ConcurrentLogsQueue queue;

  @Before
  public void before() {
    this.queue = new ConcurrentLogsQueue();
  }

  @After
  public void after() {
    this.queue.clear();
  }

  @Test
  public void shouldPublishGenerateLogs() {
    final var log = queue.publish(SHELL_COMMAND, CommandLogStatus.RUNNING, "text");
    assertThat(log.getCommand()).isEqualTo(SHELL_COMMAND);
    assertThat(log.getText()).isEqualTo("text");
    assertThat(log.getStatus()).isEqualTo(CommandLogStatus.RUNNING);
  }

  @Test
  public void shouldDispatchLogsToListeners() throws InterruptedException {
    final var list = ImmutableList.<CommandLog>builder();
    queue.init();
    Flux.<CommandLog>create(listener -> queue.addListener("listener", "app", listener))
        .subscribe(list::add);
    queue.publish(SHELL_COMMAND, CommandLogStatus.INITIALIZED, "");
    queue.publish(SHELL_COMMAND, CommandLogStatus.RUNNING, "text");
    queue.publish(Command.builder()
        .id("id")
        .applicationId("otherApp")
        .path(".")
        .environment(ImmutableMap.of())
        .command(Arrays.asList("java", "--version"))
        .onCancel(Collections.emptyList())
        .build(), CommandLogStatus.RUNNING, "text");
    queue.publish(SHELL_COMMAND, CommandLogStatus.CLOSED, "");
    Thread.sleep(2 * ConcurrentLogsQueue.INTERVAL.toMillis());
    queue.removeListener("listener");
    assertThat(list.build().size()).isEqualTo(3);
  }
}
