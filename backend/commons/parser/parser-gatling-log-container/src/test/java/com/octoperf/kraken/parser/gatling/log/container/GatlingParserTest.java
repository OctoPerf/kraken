package com.octoperf.kraken.parser.gatling.log.container;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.testing.NullPointerTester;
import com.octoperf.kraken.analysis.entity.DebugEntryTest;
import com.octoperf.kraken.config.gatling.api.GatlingLog;
import com.octoperf.kraken.config.gatling.api.GatlingProperties;
import com.octoperf.kraken.parser.debug.entry.writer.api.DebugEntryWriter;
import com.octoperf.kraken.parser.gatling.log.api.LogParser;
import com.octoperf.kraken.runtime.command.Command;
import com.octoperf.kraken.runtime.command.CommandService;
import com.octoperf.kraken.runtime.container.predicate.TaskPredicate;
import com.octoperf.kraken.runtime.container.test.AbstractContainerExecutorTest;
import com.octoperf.kraken.runtime.entity.task.FlatContainerTest;
import com.octoperf.kraken.runtime.entity.task.TaskTest;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.time.Duration;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GatlingParserTest extends AbstractContainerExecutorTest {

  @Mock
  DebugEntryWriter writer;
  @Mock
  CommandService commandService;
  @Mock
  LogParser logParser;
  @Mock
  TaskPredicate taskPredicate;
  @Mock(lenient = true)
  GatlingLog log;
  @Mock(lenient = true)
  GatlingProperties gatling;

  GatlingParser parser;

  @BeforeEach
  public void before() {
    when(gatling.getHome()).thenReturn(".");
    when(gatling.getLogs()).thenReturn(log);
    when(log.getDebug()).thenReturn(".");
    parser = new GatlingParser(
        logParser,
        writer,
        gatling,
        taskPredicate,
        commandService,
        containerExecutor);
  }

  @Test
  public void shouldInit() {
    given(logParser.parse(any())).willReturn(Flux.empty());
    given(runtimeClient.waitForPredicate(FlatContainerTest.CONTAINER, taskPredicate)).willReturn(Mono.delay(Duration.ofSeconds(1)).map(aLong -> TaskTest.TASK));
    given(commandService.execute(any(Command.class))).willReturn(Flux.just("cmd", "exec", "logs"));
    final var entries = ImmutableList.builder();
    given(writer.write(any())).willReturn(Flux.interval(Duration.ofMillis(400)).map(aLong -> DebugEntryTest.DEBUG_ENTRY).doOnNext(entries::add));
    parser.init();
    verify(runtimeClient).waitForPredicate(eq(FlatContainerTest.CONTAINER), any());
    verify(logParser).parse(any(Path.class));
    verify(writer).write(any());
    verify(commandService).execute(Command.builder()
        .path(gatling.getHome())
        .command(ImmutableList.of("ls", "-lR"))
        .environment(ImmutableMap.of())
        .build());
    assertThat(entries.build().size()).isBetween(38, 40);
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(GatlingParser.class);
  }
}
