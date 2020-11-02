package com.octoperf.kraken.gatling.container.recorder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.config.api.LocalRemoteProperties;
import com.octoperf.kraken.config.gatling.api.GatlingProperties;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.command.entity.CommandTest;
import com.octoperf.kraken.runtime.container.test.AbstractContainerExecutorTest;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.entity.StorageWatcherEventTest;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GatlingRecorderTest extends AbstractContainerExecutorTest {

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

  GatlingRecorder recorder;

  @BeforeEach
  public void before() {
    given(commandSupplier.get()).willReturn(CommandTest.SHELL_COMMAND);
    when(gatlingProperties.getHome()).thenReturn("gatlingHome");
    when(gatlingProperties.getConf()).thenReturn(gatlingLocalRemote);
    when(gatlingProperties.getHarPath()).thenReturn(gatlingLocalRemote);
    when(gatlingProperties.getUserFiles()).thenReturn(gatlingLocalRemote);
    when(gatlingLocalRemote.getLocal()).thenReturn("local");
    when(gatlingLocalRemote.getRemote()).thenReturn("remote");
    recorder = new GatlingRecorder(Mono.just(storageClient),
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
    given(storageClient.uploadFile(any(Path.class), any())).willReturn(Flux.just(StorageWatcherEventTest.STORAGE_WATCHER_EVENT));
    given(commandService.validate(any(Command.class))).willAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0, Command.class)));
    given(commandService.execute(any(Command.class))).willReturn(Flux.just("cmd", "exec", "logs"));
    recorder.init();
    verify(storageClient).downloadFolder(any(Path.class), any());
    verify(storageClient).downloadFile(any(Path.class), any());
    verify(storageClient).uploadFile(any(Path.class), any());
    verify(commandService).execute(Command.builder()
        .path(gatlingProperties.getHome())
        .args(ImmutableList.of("ls", "-lR"))
        .environment(ImmutableMap.of())
        .build());
    verify(commandService).execute(CommandTest.SHELL_COMMAND);

  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(GatlingRecorder.class);
  }
}
