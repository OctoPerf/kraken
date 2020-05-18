package com.kraken.runtime.client.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.kraken.Application;
import com.kraken.config.runtime.client.api.RuntimeClientProperties;
import com.kraken.runtime.client.api.RuntimeClient;
import com.kraken.runtime.entity.log.LogTest;
import com.kraken.runtime.entity.task.*;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.security.authentication.api.ExchangeFilterFactory;
import com.kraken.security.entity.owner.ApplicationOwner;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class WebRuntimeClientTest {

  private ObjectMapper mapper;
  private MockWebServer server;
  private RuntimeClient client;

  @Autowired
  List<ExchangeFilterFactory> filterFactories;
  @MockBean
  RuntimeClientProperties properties;

  @Before
  public void setUp() {
    server = new MockWebServer();
    mapper = new ObjectMapper();
    final String url = server.url("/").toString();
    given(properties.getUrl()).willReturn(url);
    client = new WebRuntimeClientBuilder(filterFactories, properties).mode(AuthenticationMode.NOOP).build();
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  public void shouldSetStatus() throws InterruptedException {
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    );

    final var set1 = client.setStatus(FlatContainerTest.CONTAINER, ContainerStatus.DONE);
    final var set2 = client.setStatus(FlatContainerTest.CONTAINER, ContainerStatus.RUNNING);

    set1.block();
    final var request = server.takeRequest();
    assertThat(request.getPath()).isEqualTo("/container/status/DONE?taskId=taskId&containerId=id&containerName=name");

    set2.block();
    assertThat(server.getRequestCount()).isEqualTo(1);
    assertThat(((WebRuntimeClient) client).getLastStatus()).isEqualTo(ContainerStatus.DONE);
  }

  @Test
  public void shouldWaitForStatus() throws InterruptedException, IOException {
    final var expectedStatus = ContainerStatus.READY;
    final var flatContainer = FlatContainerTest.CONTAINER;
    final var taskId = flatContainer.getTaskId();
    final var appOwner = ApplicationOwner.builder().applicationId("app").build();
    final var task = Task.builder()
        .id(taskId)
        .startDate(42L)
        .status(expectedStatus)
        .type(TaskType.GATLING_RUN)
        .containers(ImmutableList.of())
        .description("description")
        .expectedCount(2)
        .owner(appOwner)
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
        .owner(appOwner)
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

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
            .setBody(body));

    final var response = client.waitForStatus(flatContainer, expectedStatus).block();
    assertThat(response).isEqualTo(task);

    final var request = server.takeRequest();
    assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.TEXT_EVENT_STREAM_VALUE);
    assertThat(request.getPath()).isEqualTo("/task/watch");
  }

  @Test
  public void shouldFind() throws InterruptedException, JsonProcessingException {
    final var container = FlatContainerTest.CONTAINER;

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(container))
    );

    final var result = client.find("taskId", "containerName").block();

    final var request = server.takeRequest();
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

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
            .setBody(body));

    final var response = client.watchLogs().collectList().block();
    assertThat(response).isNotNull();
    assertThat(response.size()).isEqualTo(4);

    final var request = server.takeRequest();
    assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.TEXT_EVENT_STREAM_VALUE);
    assertThat(request.getPath()).isEqualTo("/logs/watch");
  }

}
