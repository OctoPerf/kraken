package com.octoperf.kraken.gatling.container.runner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.testing.NullPointerTester;
import com.octoperf.kraken.config.api.LocalRemoteProperties;
import com.octoperf.kraken.config.gatling.api.GatlingProperties;
import com.octoperf.kraken.runtime.command.Command;
import com.octoperf.kraken.runtime.command.CommandService;
import com.octoperf.kraken.runtime.command.CommandTest;
import com.octoperf.kraken.runtime.container.test.AbstractContainerExecutorTest;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.entity.StorageNodeTest;
import com.octoperf.kraken.storage.entity.StorageWatcherEventTest;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
  @Mock(lenient = true)
  Supplier<Command> commandSupplier;

  @Mock(lenient = true)
  GatlingProperties gatlingProperties;
  @Mock(lenient = true)
  LocalRemoteProperties gatlingLocalRemote;

  GatlingRunner runner;

  @BeforeEach
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
    runner = new GatlingRunner(Mono.just(storageClient),
        commandService,
        containerProperties,
        gatlingProperties,
        commandSupplier,
        containerExecutor);
  }

  @Test
  public void shouldInit() {
    given(storageClient.downloadFolder(any(Path.class), any())).willReturn(Mono.fromCallable(() -> null));
    given(storageClient.uploadFile(any(Path.class), any())).willReturn(Flux.just(StorageWatcherEventTest.STORAGE_WATCHER_EVENT));
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
    TestUtils.shouldPassNPE(GatlingRunner.class);
  }
}
