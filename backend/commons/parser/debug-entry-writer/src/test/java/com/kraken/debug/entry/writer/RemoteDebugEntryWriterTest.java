package com.kraken.debug.entry.writer;

import com.kraken.analysis.client.api.AnalysisClient;
import com.kraken.analysis.client.api.AnalysisClientBuilder;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.config.runtime.container.api.ContainerProperties;
import com.kraken.security.authentication.api.AuthenticationMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.kraken.analysis.entity.DebugEntryTest.DEBUG_ENTRY;
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
    given(clientBuilder.build()).willReturn(client);
    writer = new RemoteDebugEntryWriter(containerProperties, clientBuilder);
  }

  @Test
  public void shouldWriteEntry() {
    given(client.debug(any(DebugEntry.class))).willReturn(Mono.just(DEBUG_ENTRY));
    writer.write(Flux.just(DEBUG_ENTRY, DEBUG_ENTRY)).blockLast();
    verify(client, times(2)).debug(DEBUG_ENTRY.withResultId("taskId"));
  }
}