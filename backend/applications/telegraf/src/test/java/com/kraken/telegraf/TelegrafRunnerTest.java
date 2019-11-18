package com.kraken.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.command.CommandTest;
import com.kraken.runtime.container.predicate.TaskPredicate;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.container.properties.RuntimeContainerPropertiesTest;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.ContainerTest;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskTest;
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
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

  RuntimeContainerProperties containerProperties;

  TelegrafProperties telegrafProperties;

  TelegrafRunner runner;

  @Before
  public void before() {
    given(commandSupplier.get()).willReturn(CommandTest.SHELL_COMMAND);
    containerProperties = RuntimeContainerPropertiesTest.RUNTIME_PROPERTIES;
    telegrafProperties = TelegrafPropertiesTest.TELEGRAF_PROPERTIES;
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
    given(runtimeClient.setStatus(anyString(), anyString(), anyString(), any(ContainerStatus.class))).willReturn(Mono.fromCallable(() -> null));
    final var entries = ImmutableList.builder();
    given(commandService.execute(any(Command.class))).willReturn(Flux.just("conf", "content"));
    given(commandService.execute(CommandTest.SHELL_COMMAND)).willReturn(Flux.interval(Duration.ofMillis(400)).map(Object::toString).doOnNext(entries::add));
    given(runtimeClient.waitForPredicate(taskPredicate)).willReturn(Mono.delay(Duration.ofSeconds(1)).map(aLong -> TaskTest.TASK));
    given(storageClient.downloadFile(any(Path.class), any())).willReturn(Mono.fromCallable(() -> null));

    runner.init();

    verify(runtimeClient).setStatus(containerProperties.getTaskId(), containerProperties.getHostname(), containerProperties.getContainerId(), ContainerStatus.PREPARING);
    verify(storageClient).downloadFile(any(Path.class), any());
    verify(runtimeClient).setStatus(containerProperties.getTaskId(), containerProperties.getHostname(), containerProperties.getContainerId(), ContainerStatus.RUNNING);
    verify(runtimeClient).setStatus(containerProperties.getTaskId(), containerProperties.getHostname(), containerProperties.getContainerId(), ContainerStatus.DONE);
    verify(commandService).execute(CommandTest.SHELL_COMMAND);
    verify(runtimeClient).waitForPredicate(taskPredicate);
    assertThat(entries.build().size()).isBetween(14, 15);
  }

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester()
        .setDefault(RuntimeContainerProperties.class, containerProperties)
        .setDefault(TelegrafProperties.class, telegrafProperties)
        .testConstructors(TelegrafRunner.class, PACKAGE);
  }
}
