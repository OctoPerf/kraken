package com.octoperf.kraken.parser.har;

import com.google.common.testing.NullPointerTester;
import com.octoperf.kraken.analysis.entity.DebugEntryTest;
import com.octoperf.kraken.config.api.ApplicationProperties;
import com.octoperf.kraken.config.har.parser.api.HarParserProperties;
import com.octoperf.kraken.parser.debug.entry.writer.api.DebugEntryWriter;
import com.octoperf.kraken.parser.har.api.HarParser;
import com.octoperf.kraken.runtime.command.Command;
import com.octoperf.kraken.runtime.command.CommandService;
import com.octoperf.kraken.runtime.container.test.AbstractContainerExecutorTest;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.tests.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
  @Mock(lenient = true)
  ApplicationProperties application;

  HarParserService parser;

  @BeforeEach
  public void before() {
    when(application.getData()).thenReturn("data");
    parser = new HarParserService(harParser,
        Mono.just(storageClient),
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
    TestUtils.shouldPassNPE(HarParserService.class);
  }
}
