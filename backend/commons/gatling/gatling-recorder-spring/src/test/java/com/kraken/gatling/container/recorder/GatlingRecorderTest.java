package com.kraken.gatling.container.recorder;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GatlingRecorderTest extends AbstractContainerExecutorTest {

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

  GatlingRecorder recorder;

  @Before
  public void before() {
    given(commandSupplier.get()).willReturn(CommandTest.SHELL_COMMAND);
    when(gatlingProperties.getHome()).thenReturn("gatlingHome");
    when(gatlingProperties.getConf()).thenReturn(gatlingLocalRemote);
    when(gatlingProperties.getHarPath()).thenReturn(gatlingLocalRemote);
    when(gatlingProperties.getUserFiles()).thenReturn(gatlingLocalRemote);
    when(gatlingLocalRemote.getLocal()).thenReturn("local");
    when(gatlingLocalRemote.getRemote()).thenReturn("remote");
    recorder = new GatlingRecorder(storageClient,
        commandService,
        containerProperties,
        gatlingProperties,
        commandSupplier,
        containerExecutor);
  }

  @Test
  public void shouldInit() {
    given(storageClient.downloadFolder(any(Path.class), any())).willReturn(Mono.fromCallable(() -> null));
    given(storageClient.downloadFile(any(Path.class), any())).willReturn(Mono.fromCallable(() -> null));
    given(storageClient.uploadFile(any(Path.class), any())).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));
    given(commandService.execute(any(Command.class))).willReturn(Flux.just("cmd", "exec", "logs"));
    recorder.init();
    verify(storageClient).downloadFolder(any(Path.class), any());
    verify(storageClient).downloadFile(any(Path.class), any());
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
    new NullPointerTester().testConstructors(GatlingRecorder.class, PACKAGE);
  }
}
