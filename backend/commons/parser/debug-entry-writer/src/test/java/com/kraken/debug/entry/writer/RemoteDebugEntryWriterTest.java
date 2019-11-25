package com.kraken.debug.entry.writer;

import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.analysis.entity.DebugEntryTest;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.container.properties.RuntimeContainerPropertiesTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, RuntimeContainerPropertiesTestConfiguration.class}, initializers = {ConfigFileApplicationContextInitializer.class})
@EnableAutoConfiguration
public class RemoteDebugEntryWriterTest {

  @Autowired
  DebugEntryWriter writer;

  @Autowired
  AnalysisClient client;

  @Test
  public void shouldWriteEntry() {
    given(client.debug(any(DebugEntry.class))).willReturn(Mono.just(DebugEntryTest.DEBUG_ENTRY));
    writer.write(Flux.just(DebugEntryTest.DEBUG_ENTRY, DebugEntryTest.DEBUG_ENTRY)).blockLast();
    verify(client, times(2)).debug(DebugEntryTest.DEBUG_ENTRY.withResultId("taskId"));
  }
}