package com.kraken.gatling.container.runner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.testing.NullPointerTester;
import com.kraken.config.api.LocalRemoteProperties;
import com.kraken.config.gatling.api.GatlingProperties;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.command.CommandTest;
import com.kraken.runtime.container.executor.AbstractContainerExecutorTest;
import com.kraken.storage.client.api.StorageClient;
import com.kraken.storage.entity.StorageNodeTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.function.Supplier;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class GatlingRunnerTest extends AbstractContainerExecutorTest {

  @Mock
  StorageClient storageClient;
  @Mock
  CommandService commandService;
  @Mock
  Supplier<Command> commandSupplier;

  @Mock
  GatlingProperties gatlingProperties;
  @Mock
  LocalRemoteProperties gatlingLocalRemote;

  GatlingRunner runner;

  @Before
  public void setUp() {
    super.setUp();
    given(commandSupplier.get()).willReturn(CommandTest.SHELL_COMMAND);
    when(gatlingProperties.getHome()).thenReturn("gatlingHome");
    when(gatlingProperties.getConf()).thenReturn(gatlingLocalRemote);
    when(gatlingProperties.getUserFiles()).thenReturn(gatlingLocalRemote);
    when(gatlingProperties.getLib()).thenReturn(gatlingLocalRemote);
    when(gatlingProperties.getResults()).thenReturn(gatlingLocalRemote);
    when(gatlingLocalRemote.getLocal()).thenReturn("local");
    when(gatlingLocalRemote.getRemote()).thenReturn("remote");
    runner = new GatlingRunner(storageClient,
        commandService,
        containerProperties,
        gatlingProperties,
        commandSupplier,
        containerExecutor);
  }

  @Test
  public void shouldInit() {
    given(storageClient.downloadFolder(any(Path.class), any())).willReturn(Mono.fromCallable(() -> null));
    given(storageClient.uploadFile(any(Path.class), any())).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));
    given(commandService.execute(any(Command.class))).willReturn(Flux.just("cmd", "exec", "logs"));
    runner.init();
    verify(storageClient, times(3)).downloadFolder(any(Path.class), any());
    verify(storageClient).uploadFile(any(Path.class), any());
    verify(commandService).execute(Command.builder()
        .path(gatlingProperties.getHome())
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
