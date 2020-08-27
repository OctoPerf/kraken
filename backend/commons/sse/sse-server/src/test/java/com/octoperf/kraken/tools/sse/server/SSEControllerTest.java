package com.octoperf.kraken.tools.sse.server;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.runtime.client.api.RuntimeClient;
import com.octoperf.kraken.runtime.client.api.RuntimeClientBuilder;
import com.octoperf.kraken.runtime.client.api.RuntimeWatchClient;
import com.octoperf.kraken.runtime.client.api.RuntimeWatchClientBuilder;
import com.octoperf.kraken.runtime.entity.log.LogTest;
import com.octoperf.kraken.runtime.entity.task.TaskTest;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.client.api.StorageClientBuilder;
import com.octoperf.kraken.storage.entity.StorageWatcherEventTest;
import com.octoperf.kraken.tests.web.security.AuthControllerTest;
import com.octoperf.kraken.tests.utils.TestUtils;
import com.octoperf.kraken.tools.sse.SSEService;
import com.octoperf.kraken.tools.sse.SSEWrapper;
import com.octoperf.kraken.tools.sse.SSEWrapperTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;

public class SSEControllerTest extends AuthControllerTest {

  @MockBean
  SSEService sse;
  @MockBean
  RuntimeWatchClientBuilder runtimeClientBuilder;
  @MockBean
  RuntimeWatchClient runtimeClient;
  @MockBean
  StorageClientBuilder storageClientBuilder;
  @MockBean
  StorageClient storageClient;

  @BeforeEach
  public void setUp() throws IOException {
    super.setUp();
    given(runtimeClientBuilder.build(AuthenticatedClientBuildOrder.builder().mode(AuthenticationMode.SESSION)
        .projectId(projectId)
        .applicationId(applicationId).build())).willReturn(Mono.just(runtimeClient));
    given(storageClientBuilder.build(AuthenticatedClientBuildOrder.builder().mode(AuthenticationMode.SESSION)
        .projectId(projectId)
        .applicationId(applicationId).build())).willReturn(Mono.just(storageClient));
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(SSEController.class);
  }

  @Test
  public void shouldWatch() {
    given(sse.merge(anyMap())).willReturn(Flux.just(SSEWrapperTest.WRAPPER_STRING, SSEWrapperTest.WRAPPER_INT));
    given(sse.keepAlive(Mockito.<Flux<SSEWrapper>>any())).willReturn(Flux.just(ServerSentEvent.builder(SSEWrapperTest.WRAPPER_STRING).build()));
    given(runtimeClient.watchLogs()).willReturn(Flux.just(LogTest.LOG));
    given(runtimeClient.watchTasks()).willReturn(Flux.just(ImmutableList.of(TaskTest.TASK)));
    given(storageClient.watch()).willReturn(Flux.just(StorageWatcherEventTest.STORAGE_WATCHER_EVENT));

    final var result = webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/sse/watch").build())
        .header("Authorization", "Bearer user-token")
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
        .expectBody()
        .returnResult();
    final var body = new String(Optional.ofNullable(result.getResponseBody()).orElse(new byte[0]), Charsets.UTF_8);
    Assertions.assertThat(body).isEqualTo("data:{\"type\":\"String\",\"value\":\"value\"}\n" +
        "\n");
  }

  @Test
  public void shouldFailToWatchHeader() {
    final var applicationId = "applicationId"; // Should match [a-z0-9]*
    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/sse/watch").build())
        .header("Authorization", "Bearer user-token")
        .header("ApplicationId", applicationId)
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .exchange()
        .expectStatus().is5xxServerError();
  }

  @Test
  public void shouldFailToWatchForbidden() {
    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/sse/watch").build())
        .header("Authorization", "Bearer no-role-token")
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .exchange()
        .expectStatus().is4xxClientError();
  }
}