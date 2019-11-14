package com.kraken.runtime.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.kraken.runtime.entity.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RuntimeClientTest {

  private ObjectMapper mapper;
  private MockWebServer runtimeMockWebServer;
  private RuntimeClient client;

  @Before
  public void before() {
    runtimeMockWebServer = new MockWebServer();
    mapper = new ObjectMapper();
    client = new RuntimeWebClient(WebClient.create(runtimeMockWebServer.url("/").toString()));
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

    client.setStatus("taskId", "hostId", "containerId", ContainerStatus.READY).block();

    final var request = runtimeMockWebServer.takeRequest();
    assertThat(request.getPath()).isEqualTo("/container/status/READY?taskId=taskId&hostId=hostId&containerId=containerId");
  }

  @Test
  public void shouldWaitForStatus() throws InterruptedException, IOException {

    final var expectedStatus = ContainerStatus.READY;
    final var taskId = "readyTaskId";
    final var task = Task.builder()
        .id(taskId)
        .startDate(42L)
        .status(expectedStatus)
        .type(TaskType.RUN)
        .containers(ImmutableList.of())
        .description("description")
        .expectedCount(2)
        .build();

    final var empty = ImmutableList.<Task>of();
    final var other = ImmutableList.of(TaskTest.TASK);
    final var notReady = ImmutableList.of(TaskTest.TASK, Task.builder()
        .id(taskId)
        .startDate(42L)
        .status(ContainerStatus.STARTING)
        .type(TaskType.RUN)
        .containers(ImmutableList.of())
        .description("description")
        .expectedCount(2)
        .build());
    final var ready = ImmutableList.of(TaskTest.TASK, task);

    final var body = ":keep alive\n" +
        "\n" +
        "data:"+ mapper.writeValueAsString(empty) +"\n" +
        "\n" +
        ":keep alive\n" +
        "\n" +
        "data:"+ mapper.writeValueAsString(other) +"\n" +
        "\n" +
        ":keep alive\n" +
        "\n" +
        "data:"+ mapper.writeValueAsString(notReady) +"\n" +
        "\n" +
        "data:"+ mapper.writeValueAsString(ready) +"\n" +
        "\n";

    runtimeMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
            .setBody(body));

    final var response = client.waitForStatus(taskId, expectedStatus).block();
    assertThat(response).isEqualTo(task);

    final var request = runtimeMockWebServer.takeRequest();
    assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.TEXT_EVENT_STREAM_VALUE);
    assertThat(request.getPath()).isEqualTo("/task/watch");
  }

}
