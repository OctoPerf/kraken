package com.kraken.analysis.container.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import com.kraken.config.telegraf.api.TelegrafProperties;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.command.CommandTest;
import com.kraken.runtime.container.predicate.TaskPredicate;
import com.kraken.runtime.container.properties.ContainerProperties;
import com.kraken.runtime.container.properties.ContainerPropertiesTest;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.FlatContainerTest;
import com.kraken.runtime.entity.task.TaskTest;
import com.kraken.storage.client.StorageClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

@RunWith(MockitoJUnitRunner.class)
public class TelegrafRunnerTest {

  @Mock
  RuntimeClient runtimeClient;
  @Mock
  CommandService commandService;
  @Mock
  StorageClient storageClient;
  @Mock
  Supplier<Command> commandSupplier;
  @Mock
  TaskPredicate taskPredicate;

  ContainerProperties containerProperties;

  @Mock
  TelegrafProperties telegrafProperties;

  TelegrafRunner runner;

  @Before
  public void before() {
    given(commandSupplier.get()).willReturn(CommandTest.SHELL_COMMAND);
    containerProperties = ContainerPropertiesTest.RUNTIME_PROPERTIES;
    when(telegrafProperties.getLocal()).thenReturn("localPath");
    when(telegrafProperties.getRemote()).thenReturn("remotePath");
    runner = new TelegrafRunner(
        runtimeClient,
        commandService,
        storageClient,
        containerProperties,
        telegrafProperties,
        commandSupplier,
        taskPredicate);
  }

  @Test
  public void shouldInit() throws InterruptedException {
    given(runtimeClient.find(containerProperties.getTaskId(), containerProperties.getName())).willReturn(Mono.just(FlatContainerTest.CONTAINER));
    given(runtimeClient.setStatus(any(FlatContainer.class), any(ContainerStatus.class))).willReturn(Mono.fromCallable(() -> null));
    final var entries = ImmutableList.builder();
    given(commandService.execute(any(Command.class))).willReturn(Flux.just("conf", "content"));
    given(commandService.execute(CommandTest.SHELL_COMMAND)).willReturn(Flux.interval(Duration.ofMillis(400)).map(Object::toString).doOnNext(entries::add));
    given(runtimeClient.waitForPredicate(FlatContainerTest.CONTAINER, taskPredicate)).willReturn(Mono.delay(Duration.ofSeconds(1)).map(aLong -> TaskTest.TASK));
    given(storageClient.downloadFile(any(Path.class), any())).willReturn(Mono.fromCallable(() -> null));

    runner.init();

    verify(runtimeClient).setStatus(FlatContainerTest.CONTAINER, ContainerStatus.PREPARING);
    verify(storageClient).downloadFile(any(Path.class), any());
    verify(runtimeClient).setStatus(FlatContainerTest.CONTAINER, ContainerStatus.RUNNING);
    verify(runtimeClient).setStatus(FlatContainerTest.CONTAINER, ContainerStatus.DONE);
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
