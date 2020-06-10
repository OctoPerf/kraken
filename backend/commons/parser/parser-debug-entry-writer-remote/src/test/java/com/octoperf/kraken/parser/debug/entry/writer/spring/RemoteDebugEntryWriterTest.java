package com.octoperf.kraken.parser.debug.entry.writer.spring;

import com.octoperf.kraken.analysis.client.api.AnalysisClient;
import com.octoperf.kraken.analysis.client.api.AnalysisClientBuilder;
import com.octoperf.kraken.analysis.entity.DebugEntry;
import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.parser.debug.entry.writer.api.DebugEntryWriter;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.octoperf.kraken.analysis.entity.DebugEntryTest.DEBUG_ENTRY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RemoteDebugEntryWriterTest {

  @Mock
  AnalysisClientBuilder clientBuilder;
  @Mock
  AnalysisClient client;
  @Mock
  ContainerProperties containerProperties;

  DebugEntryWriter writer;

  @BeforeEach
  public void setUp() {
    given(containerProperties.getApplicationId()).willReturn("app");
    given(containerProperties.getTaskId()).willReturn("taskId");
    given(clientBuilder.mode(AuthenticationMode.CONTAINER)).willReturn(clientBuilder);
    given(clientBuilder.applicationId("app")).willReturn(clientBuilder);
    given(clientBuilder.build()).willReturn(Mono.just(client));
    writer = new RemoteDebugEntryWriter(containerProperties, clientBuilder);
  }

  @Test
  public void shouldWriteEntry() {
    given(client.debug(any(DebugEntry.class))).willReturn(Mono.just(DEBUG_ENTRY));
    writer.write(Flux.just(DEBUG_ENTRY, DEBUG_ENTRY)).blockLast();
    verify(client, times(2)).debug(DEBUG_ENTRY.withResultId("taskId"));
  }
}