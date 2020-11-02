package com.octoperf.kraken.analysis.container.telegraf;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.config.telegraf.api.TelegrafProperties;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.command.entity.CommandTest;
import com.octoperf.kraken.runtime.container.predicate.TaskPredicate;
import com.octoperf.kraken.runtime.container.test.AbstractContainerExecutorTest;
import com.octoperf.kraken.runtime.entity.task.FlatContainerTest;
import com.octoperf.kraken.runtime.entity.task.TaskTest;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.time.Duration;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TelegrafRunnerTest extends AbstractContainerExecutorTest {

  @Mock
  CommandService commandService;
  @Mock
  StorageClient storageClient;
  @Mock(lenient = true)
  Supplier<Command> commandSupplier;
  @Mock
  TaskPredicate taskPredicate;

  @Mock(lenient = true)
  TelegrafProperties telegrafProperties;

  TelegrafRunner runner;

  @BeforeEach
  public void setUp() {
    super.setUp();
    given(commandSupplier.get()).willReturn(CommandTest.SHELL_COMMAND);
    when(telegrafProperties.getLocal()).thenReturn("localPath");
    when(telegrafProperties.getRemote()).thenReturn("remotePath");
    runner = new TelegrafRunner(
        commandService,
        Mono.just(storageClient),
        telegrafProperties,
        commandSupplier,
        taskPredicate,
        containerExecutor);
  }

  @Test
  public void shouldInit() {
    final var entries = ImmutableList.builder();
    given(commandService.validate(any(Command.class))).willAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0, Command.class)));
    given(commandService.execute(any(Command.class))).willReturn(Flux.just("conf", "content"));
    given(commandService.execute(CommandTest.SHELL_COMMAND)).willReturn(Flux.interval(Duration.ofMillis(400)).map(Object::toString).doOnNext(entries::add));
    given(runtimeClient.waitForPredicate(FlatContainerTest.CONTAINER, taskPredicate)).willReturn(Mono.delay(Duration.ofSeconds(1)).map(aLong -> TaskTest.TASK));
    given(storageClient.downloadFile(any(Path.class), any())).willReturn(Mono.fromCallable(() -> null));

    runner.init();

    verify(storageClient).downloadFile(any(Path.class), any());
    verify(commandService).execute(CommandTest.SHELL_COMMAND);
    verify(runtimeClient).waitForPredicate(FlatContainerTest.CONTAINER, taskPredicate);
    assertThat(entries.build().size()).isBetween(14, 15);
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(TelegrafRunner.class);
  }
}
