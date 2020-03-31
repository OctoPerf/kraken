package com.kraken.parser.har;

import com.google.common.testing.NullPointerTester;
import com.kraken.analysis.entity.DebugEntryTest;
import com.kraken.config.api.ApplicationProperties;
import com.kraken.config.har.parser.api.HarParserProperties;
import com.kraken.debug.entry.writer.DebugEntryWriter;
import com.kraken.har.parser.HarParser;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.properties.ContainerProperties;
import com.kraken.runtime.container.properties.ContainerPropertiesTest;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.FlatContainerTest;
import com.kraken.runtime.entity.task.TaskTest;
import com.kraken.storage.client.StorageClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class HarParserServiceTest {

  @Mock
  DebugEntryWriter writer;
  @Mock
  StorageClient storageClient;
  @Mock
  RuntimeClient runtimeClient;
  @Mock
  CommandService commandService;
  @Mock
  HarParser harParser;
  ContainerProperties containerProperties;
  @Mock
  HarParserProperties harParserProperties;
  @Mock
  ApplicationProperties application;

  HarParserService parser;

  @Before
  public void before() {
    when(application.getData()).thenReturn("data");
    containerProperties = ContainerPropertiesTest.RUNTIME_PROPERTIES;
    parser = new HarParserService(harParser,
        runtimeClient,
        storageClient,
        commandService,
        writer,
        containerProperties,
        harParserProperties,
      application);
  }

  @Test
  public void shouldInit() {
    given(runtimeClient.find(containerProperties.getTaskId(), containerProperties.getName())).willReturn(Mono.just(FlatContainerTest.CONTAINER));
    given(runtimeClient.setStatus(any(FlatContainer.class), any(ContainerStatus.class))).willReturn(Mono.fromCallable(() -> null));
    given(runtimeClient.waitForStatus(any(), any(ContainerStatus.class))).willReturn(Mono.just(TaskTest.TASK));
    given(harParser.parse(any())).willReturn(Flux.empty());
    given(storageClient.downloadFile(any(Path.class), any())).willReturn(Mono.fromCallable(() -> null));
    given(writer.write(any())).willReturn(Flux.just(DebugEntryTest.DEBUG_ENTRY, DebugEntryTest.DEBUG_ENTRY, DebugEntryTest.DEBUG_ENTRY));
    given(commandService.execute(any(Command.class))).willReturn(Flux.just("cmd", "exec", "logs"));
    given(harParserProperties.getLocal()).willReturn("localHarPath");
    given(harParserProperties.getRemote()).willReturn("remoteHarPath");
    parser.init();
    verify(runtimeClient).setStatus(FlatContainerTest.CONTAINER, ContainerStatus.PREPARING);
    verify(runtimeClient).setStatus(FlatContainerTest.CONTAINER, ContainerStatus.READY);
    verify(runtimeClient).setStatus(FlatContainerTest.CONTAINER, ContainerStatus.RUNNING);
    verify(runtimeClient).setStatus(FlatContainerTest.CONTAINER, ContainerStatus.DONE);
    verify(runtimeClient).waitForStatus(FlatContainerTest.CONTAINER, ContainerStatus.READY);
    verify(harParser).parse(any(Path.class));
    verify(writer).write(any());
    verify(storageClient).downloadFile(Path.of("localHarPath"), "remoteHarPath");
  }

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester().testConstructors(HarParserService.class, PACKAGE);
  }
}
