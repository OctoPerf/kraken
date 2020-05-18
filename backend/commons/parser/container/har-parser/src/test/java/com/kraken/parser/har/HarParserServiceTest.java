package com.kraken.parser.har;

import com.google.common.testing.NullPointerTester;
import com.kraken.analysis.entity.DebugEntryTest;
import com.kraken.config.api.ApplicationProperties;
import com.kraken.config.har.parser.api.HarParserProperties;
import com.kraken.debug.entry.writer.DebugEntryWriter;
import com.kraken.har.parser.HarParser;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.executor.AbstractContainerExecutorTest;
import com.kraken.storage.client.api.StorageClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
public class HarParserServiceTest extends AbstractContainerExecutorTest {

  @Mock
  DebugEntryWriter writer;
  @Mock
  StorageClient storageClient;
  @Mock
  CommandService commandService;
  @Mock
  HarParser harParser;
  @Mock
  HarParserProperties harParserProperties;
  @Mock
  ApplicationProperties application;

  HarParserService parser;

  @Before
  public void before() {
    when(application.getData()).thenReturn("data");
    parser = new HarParserService(harParser,
        storageClient,
        commandService,
        writer,
        harParserProperties,
        application,
        containerExecutor);
  }

  @Test
  public void shouldInit() {
    given(harParser.parse(any())).willReturn(Flux.empty());
    given(storageClient.downloadFile(any(Path.class), any())).willReturn(Mono.fromCallable(() -> null));
    given(writer.write(any())).willReturn(Flux.just(DebugEntryTest.DEBUG_ENTRY, DebugEntryTest.DEBUG_ENTRY, DebugEntryTest.DEBUG_ENTRY));
    given(commandService.execute(any(Command.class))).willReturn(Flux.just("cmd", "exec", "logs"));
    given(harParserProperties.getLocal()).willReturn("localHarPath");
    given(harParserProperties.getRemote()).willReturn("remoteHarPath");
    parser.init();
    verify(harParser).parse(any(Path.class));
    verify(writer).write(any());
    verify(storageClient).downloadFile(Path.of("localHarPath"), "remoteHarPath");
  }

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester().testConstructors(HarParserService.class, PACKAGE);
  }
}
