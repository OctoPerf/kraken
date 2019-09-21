package com.kraken.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.command.CommandTest;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.container.properties.RuntimeContainerPropertiesTest;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.ContainerTest;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
  Supplier<Command> commandSupplier;
  @Mock
  Predicate<Task> taskPredicate;;
  RuntimeContainerProperties containerProperties;

  TelegrafRunner runner;

  @Before
  public void before() {
    given(commandSupplier.get()).willReturn(CommandTest.SHELL_COMMAND);
    containerProperties = RuntimeContainerPropertiesTest.RUNTIME_PROPERTIES;
    runner = new TelegrafRunner(
        runtimeClient,
        commandService,
        containerProperties,
        commandSupplier,
        taskPredicate);
  }

  @Test
  public void shouldInit() {
    given(runtimeClient.setStatus(anyString(), any(ContainerStatus.class))).willReturn(Mono.just(ContainerTest.CONTAINER));
//    given(runtimeClient.waitForStatus(anyString(), any(ContainerStatus.class))).willReturn(Mono.just(TaskTest.TASK));
    final var entries = ImmutableList.builder();
    given(commandService.execute(any(Command.class))).willReturn(Flux.interval(Duration.ofMillis(400)).map(Object::toString).doOnNext(entries::add));
    given(runtimeClient.waitForPredicate(taskPredicate)).willReturn(Mono.delay(Duration.ofSeconds(1)).map(aLong -> TaskTest.TASK));

    runner.init();

//    verify(runtimeClient).setStatus(containerProperties.getContainerId(), ContainerStatus.READY);
    verify(runtimeClient).setStatus(containerProperties.getContainerId(), ContainerStatus.RUNNING);
    verify(runtimeClient).setStatus(containerProperties.getContainerId(), ContainerStatus.DONE);
//    verify(runtimeClient).waitForStatus(containerProperties.getTaskId(), ContainerStatus.READY);
    verify(commandService).execute(CommandTest.SHELL_COMMAND);
    verify(runtimeClient).waitForPredicate(taskPredicate);
    assertThat(entries.build().size()).isBetween(14, 15);
  }

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester()
        .setDefault(RuntimeContainerProperties.class, containerProperties)
        .testConstructors(TelegrafRunner.class, PACKAGE);
  }
}
