package com.kraken.debug.entry.writer;

import com.kraken.Application;
import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.runtime.container.properties.ContainerProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.kraken.analysis.entity.DebugEntryTest.DEBUG_ENTRY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RemoteDebugEntryWriterTest {
  @Autowired
  DebugEntryWriter writer;
  @MockBean
  AnalysisClient client;

  @Test
  public void shouldWriteEntry() {
    given(client.debug(any(DebugEntry.class))).willReturn(Mono.just(DEBUG_ENTRY));
    writer.write(Flux.just(DEBUG_ENTRY, DEBUG_ENTRY)).blockLast();
    verify(client, times(2)).debug(DEBUG_ENTRY.withResultId("taskId"));
  }
}