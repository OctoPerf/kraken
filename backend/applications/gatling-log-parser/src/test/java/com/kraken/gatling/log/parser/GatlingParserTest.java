package com.kraken.gatling.log.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.testing.NullPointerTester;
import com.kraken.analysis.entity.DebugEntryTest;
import com.kraken.debug.entry.writer.DebugEntryWriter;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.predicate.TaskPredicate;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.container.properties.RuntimeContainerPropertiesTest;
import com.kraken.runtime.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.time.Duration;
import java.util.function.Predicate;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class GatlingParserTest {

  @Mock
  DebugEntryWriter writer;
  @Mock
  RuntimeClient runtimeClient;
  @Mock
  CommandService commandService;
  @Mock
  LogParser logParser;
  @Mock
  TaskPredicate taskPredicate;

  RuntimeContainerProperties containerProperties;
  GatlingParserProperties gatlingParserProperties;

  GatlingParser parser;

  @Before
  public void before() {
    containerProperties = RuntimeContainerPropertiesTest.RUNTIME_PROPERTIES;
    gatlingParserProperties = GatlingParserPropertiesTest.GATLING_PROPERTIES;
    parser = new GatlingParser(logParser,
        runtimeClient,
        writer,
        containerProperties,
        gatlingParserProperties,
        taskPredicate,
        commandService);
  }

  @Test
  public void shouldInit() throws InterruptedException {
    given(runtimeClient.find(containerProperties.getTaskId(), containerProperties.getContainerName())).willReturn(Mono.just(FlatContainerTest.CONTAINER));
    given(runtimeClient.setFailedStatus(any(FlatContainer.class))).willReturn(Mono.fromCallable(() -> "failed"));
    given(runtimeClient.setStatus(any(FlatContainer.class), any(ContainerStatus.class))).willReturn(Mono.fromCallable(() -> null));
    given(runtimeClient.waitForStatus(anyString(), any(ContainerStatus.class))).willReturn(Mono.just(TaskTest.TASK));
    given(logParser.parse(any())).willReturn(Flux.empty());
    given(runtimeClient.waitForPredicate(taskPredicate)).willReturn(Mono.delay(Duration.ofSeconds(1)).map(aLong -> TaskTest.TASK));
    given(commandService.execute(any(Command.class))).willReturn(Flux.just("cmd", "exec", "logs"));
    final var entries = ImmutableList.builder();
    given(writer.write(any())).willReturn(Flux.interval(Duration.ofMillis(400)).map(aLong -> DebugEntryTest.DEBUG_ENTRY).doOnNext(entries::add));
    parser.init();
    verify(runtimeClient).setStatus(FlatContainerTest.CONTAINER, ContainerStatus.READY);
    verify(runtimeClient).setStatus(FlatContainerTest.CONTAINER, ContainerStatus.RUNNING);
    verify(runtimeClient).setStatus(FlatContainerTest.CONTAINER, ContainerStatus.DONE);
    verify(runtimeClient).waitForStatus(containerProperties.getTaskId(), ContainerStatus.READY);
    verify(runtimeClient).waitForPredicate(any());
    verify(logParser).parse(any(Path.class));
    verify(writer).write(any());
    verify(commandService).execute(Command.builder()
        .path(gatlingParserProperties.getGatlingHome().toString())
        .command(ImmutableList.of("ls", "-lR"))
        .environment(ImmutableMap.of())
        .build());
    assertThat(entries.build().size()).isBetween(38, 40);
  }

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester()
        .setDefault(RuntimeContainerProperties.class, containerProperties)
        .setDefault(GatlingParserProperties.class, gatlingParserProperties)
        .testConstructors(GatlingParser.class, PACKAGE);
  }
}
