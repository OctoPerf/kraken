package com.kraken.runtime.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.kraken.config.runtime.client.api.RuntimeClientProperties;
import com.kraken.runtime.entity.log.LogTest;
import com.kraken.runtime.entity.task.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RuntimeWebClientTest {

  private ObjectMapper mapper;
  private MockWebServer runtimeMockWebServer;
  private RuntimeWebClient client;

  @Mock
  RuntimeClientProperties properties;

  @Before
  public void before() {
    runtimeMockWebServer = new MockWebServer();
    mapper = new ObjectMapper();
    final String url = runtimeMockWebServer.url("/").toString();
    when(properties.getUrl()).thenReturn(url);
    client = new RuntimeWebClient(properties);
  }

  @After
  public void tearDown() throws IOException {
    runtimeMockWebServer.shutdown();
  }

  @Test
  public void shouldSetStatus() throws InterruptedException {
    runtimeMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    );

    final var set1 = client.setStatus(FlatContainerTest.CONTAINER, ContainerStatus.DONE);
    final var set2 = client.setStatus(FlatContainerTest.CONTAINER, ContainerStatus.RUNNING);

    set1.block();
    final var request = runtimeMockWebServer.takeRequest();
    assertThat(request.getPath()).isEqualTo("/container/status/DONE?taskId=taskId&containerId=id&containerName=name");

    set2.block();
    assertThat(runtimeMockWebServer.getRequestCount()).isEqualTo(1);
    assertThat(client.getLastStatus()).isEqualTo(ContainerStatus.DONE);
  }

  @Test
  public void shouldSetFailedStatus() throws InterruptedException {
    runtimeMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    );

    client.setFailedStatus(FlatContainerTest.CONTAINER).block();

    final var request = runtimeMockWebServer.takeRequest();
    assertThat(request.getPath()).isEqualTo("/container/status/FAILED?taskId=taskId&containerId=id&containerName=name");

    client.setStatus(FlatContainerTest.CONTAINER, ContainerStatus.DONE);
    assertThat(runtimeMockWebServer.getRequestCount()).isEqualTo(1);
    assertThat(client.getLastStatus()).isEqualTo(ContainerStatus.FAILED);
  }

  @Test
  public void shouldWaitForStatus() throws InterruptedException, IOException {
    final var expectedStatus = ContainerStatus.READY;
    final var flatContainer = FlatContainerTest.CONTAINER;
    final var taskId = flatContainer.getTaskId();
    final var task = Task.builder()
        .id(taskId)
        .startDate(42L)
        .status(expectedStatus)
        .type(TaskType.GATLING_RUN)
        .containers(ImmutableList.of())
        .description("description")
        .expectedCount(2)
        .applicationId("app")
        .build();

    final var empty = ImmutableList.<Task>of();
    final var other = ImmutableList.of(TaskTest.TASK);
    final var notReady = ImmutableList.of(TaskTest.TASK, Task.builder()
        .id(taskId)
        .startDate(42L)
        .status(ContainerStatus.STARTING)
        .type(TaskType.GATLING_RUN)
        .containers(ImmutableList.of())
        .description("description")
        .expectedCount(2)
        .applicationId("app")
        .build());
    final var ready = ImmutableList.of(TaskTest.TASK, task);

    final var body = ":keep alive\n" +
        "\n" +
        "data:" + mapper.writeValueAsString(empty) + "\n" +
        "\n" +
        ":keep alive\n" +
        "\n" +
        "data:" + mapper.writeValueAsString(other) + "\n" +
        "\n" +
        ":keep alive\n" +
        "\n" +
        "data:" + mapper.writeValueAsString(notReady) + "\n" +
        "\n" +
        "data:" + mapper.writeValueAsString(ready) + "\n" +
        "\n";

    runtimeMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
            .setBody(body));

    final var response = client.waitForStatus(flatContainer, expectedStatus).block();
    assertThat(response).isEqualTo(task);

    final var request = runtimeMockWebServer.takeRequest();
    assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.TEXT_EVENT_STREAM_VALUE);
    assertThat(request.getPath()).isEqualTo("/task/watch/app");
  }

  @Test
  public void shouldWaitForStatusFail() throws InterruptedException {
    final var flatContainer = FlatContainerTest.CONTAINER;

    // Tasks stream
    runtimeMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(500)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
            .setBody(""));

    // Set status
    runtimeMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    );

    client.waitForStatus(flatContainer, ContainerStatus.RUNNING).block();

    assertThat(client.getLastStatus()).isEqualTo(ContainerStatus.FAILED);
  }

  @Test
  public void shouldFind() throws InterruptedException, JsonProcessingException {
    final var container = FlatContainerTest.CONTAINER;

    runtimeMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(container))
    );

    final var result = client.find("taskId", "containerName").block();

    final var request = runtimeMockWebServer.takeRequest();
    assertThat(request.getMethod()).isEqualTo("GET");
    assertThat(request.getPath()).isEqualTo("/container/find?taskId=taskId&containerName=containerName");
    assertThat(result).isEqualTo(container);
  }

  @Test
  public void shouldWatchLogs() throws InterruptedException, IOException {
    final var body = ":keep alive\n" +
        "\n" +
        "data:" + mapper.writeValueAsString(LogTest.LOG) + "\n" +
        "\n" +
        ":keep alive\n" +
        "\n" +
        "data:" + mapper.writeValueAsString(LogTest.LOG) + "\n" +
        "\n" +
        ":keep alive\n" +
        "\n" +
        "data:" + mapper.writeValueAsString(LogTest.LOG) + "\n" +
        "\n" +
        "data:" + mapper.writeValueAsString(LogTest.LOG) + "\n" +
        "\n";

    runtimeMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
            .setBody(body));

    final var response = client.watchLogs("app").collectList().block();
    assertThat(response).isNotNull();
    assertThat(response.size()).isEqualTo(4);

    final var request = runtimeMockWebServer.takeRequest();
    assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.TEXT_EVENT_STREAM_VALUE);
    assertThat(request.getPath()).isEqualTo("/logs/watch/app");
  }

}
