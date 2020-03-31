package com.kraken.gatling.container.runner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.testing.NullPointerTester;
import com.kraken.config.gatling.api.GatlingProperties;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.command.CommandTest;
import com.kraken.runtime.container.properties.ContainerProperties;
import com.kraken.runtime.container.properties.ContainerPropertiesTest;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.TaskTest;
import com.kraken.storage.client.StorageClient;
import com.kraken.storage.entity.StorageNodeTest;
import com.kraken.config.api.LocalRemoteProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.function.Supplier;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.kraken.runtime.entity.task.FlatContainerTest.CONTAINER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GatlingRunnerTest {

  @Mock
  StorageClient storageClient;
  @Mock
  RuntimeClient runtimeClient;
  @Mock
  CommandService commandService;
  @Mock
  Supplier<Command> commandSupplier;
  ContainerProperties containerProperties;

  @Mock
  GatlingProperties gatling;
  @Mock
  LocalRemoteProperties gatlingLocalRemote;

  GatlingRunner runner;

  @Before
  public void before() {
    given(commandSupplier.get()).willReturn(CommandTest.SHELL_COMMAND);
    when(gatling.getHome()).thenReturn("gatlingHome");
    when(gatling.getConf()).thenReturn(gatlingLocalRemote);
    when(gatling.getUserFiles()).thenReturn(gatlingLocalRemote);
    when(gatling.getLib()).thenReturn(gatlingLocalRemote);
    when(gatling.getResults()).thenReturn(gatlingLocalRemote);
    when(gatlingLocalRemote.getLocal()).thenReturn("local");
    when(gatlingLocalRemote.getRemote()).thenReturn("remote");
    containerProperties = ContainerPropertiesTest.RUNTIME_PROPERTIES;
    runner = new GatlingRunner(storageClient,
        runtimeClient,
        commandService,
        containerProperties,
      gatling,
        commandSupplier);
  }

  @Test
  public void shouldInit() {
    given(runtimeClient.find(containerProperties.getTaskId(), containerProperties.getName())).willReturn(Mono.just(CONTAINER));
    given(runtimeClient.setStatus(any(FlatContainer.class), any(ContainerStatus.class))).willReturn(Mono.fromCallable(() -> null));
    given(runtimeClient.waitForStatus(any(), any(ContainerStatus.class))).willReturn(Mono.just(TaskTest.TASK));
    given(storageClient.downloadFolder(any(Path.class), any())).willReturn(Mono.fromCallable(() -> null));
    given(storageClient.uploadFile(any(Path.class), any())).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));
    given(commandService.execute(any(Command.class))).willReturn(Flux.just("cmd", "exec", "logs"));
    runner.init();
    verify(runtimeClient).setStatus(CONTAINER, ContainerStatus.PREPARING);
    verify(runtimeClient).setStatus(CONTAINER, ContainerStatus.READY);
    verify(runtimeClient).setStatus(CONTAINER, ContainerStatus.RUNNING);
    verify(runtimeClient).setStatus(CONTAINER, ContainerStatus.STOPPING);
    verify(runtimeClient).setStatus(CONTAINER, ContainerStatus.DONE);
    verify(runtimeClient).waitForStatus(CONTAINER, ContainerStatus.READY);
    verify(runtimeClient).waitForStatus(CONTAINER, ContainerStatus.STOPPING);
    verify(storageClient, times(3)).downloadFolder(any(Path.class), any());
    verify(storageClient).uploadFile(any(Path.class), any());
    verify(commandService).execute(Command.builder()
        .path(gatling.getHome())
        .command(ImmutableList.of("ls", "-lR"))
        .environment(ImmutableMap.of())
        .build());
    verify(commandService).execute(CommandTest.SHELL_COMMAND);

  }

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester().testConstructors(GatlingRunner.class, PACKAGE);
  }
}
