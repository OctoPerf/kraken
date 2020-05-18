package com.kraken.analysis.container.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import com.kraken.config.telegraf.api.TelegrafProperties;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.command.CommandTest;
import com.kraken.runtime.container.executor.AbstractContainerExecutorTest;
import com.kraken.runtime.container.predicate.TaskPredicate;
import com.kraken.runtime.entity.task.FlatContainerTest;
import com.kraken.runtime.entity.task.TaskTest;
import com.kraken.storage.client.api.StorageClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.time.Duration;
import java.util.function.Supplier;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
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
  @Mock
  Supplier<Command> commandSupplier;
  @Mock
  TaskPredicate taskPredicate;

  @Mock
  TelegrafProperties telegrafProperties;

  TelegrafRunner runner;

  @Before
  public void setUp() {
    super.setUp();
    given(commandSupplier.get()).willReturn(CommandTest.SHELL_COMMAND);
    when(telegrafProperties.getLocal()).thenReturn("localPath");
    when(telegrafProperties.getRemote()).thenReturn("remotePath");
    runner = new TelegrafRunner(
        runtimeClient,
        commandService,
        storageClient,
        telegrafProperties,
        commandSupplier,
        taskPredicate,
        containerExecutor);
  }

  @Test
  public void shouldInit() {
    final var entries = ImmutableList.builder();
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
    new NullPointerTester()
        .testConstructors(TelegrafRunner.class, PACKAGE);
  }
}
