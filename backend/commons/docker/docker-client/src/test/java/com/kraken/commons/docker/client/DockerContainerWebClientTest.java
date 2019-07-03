package com.kraken.commons.docker.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.kraken.commons.docker.entity.DockerContainerTest;
import com.kraken.commons.docker.entity.DockerImageTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class DockerContainerWebClientTest {

  private ObjectMapper mapper;
  private MockWebServer dockerMockWebServer;
  private DockerContainerClient client;

  @Before
  public void before() {
    dockerMockWebServer = new MockWebServer();
    mapper = new ObjectMapper();
    client = new DockerContainerWebClient(WebClient.create(dockerMockWebServer.url("/").toString()));
  }

  @After
  public void tearDown() throws IOException {
    dockerMockWebServer.shutdown();
  }

  @Test
  public void shouldRun() throws InterruptedException {
    final var containerId = "containerId";
    final var name = "name";
    final var config = "image-id: image";

    dockerMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .setBody(containerId)
    );
    final var response = client.run(name, config).block();
    assertThat(response).isEqualTo(containerId);

    final RecordedRequest dockerRequest = dockerMockWebServer.takeRequest();
    assertThat(dockerRequest.getPath()).startsWith("/container/run");
    assertThat(dockerRequest.getBody().readString(Charsets.UTF_8)).isEqualTo(config);
  }

  @Test
  public void shouldStart() throws InterruptedException {
    final var containerId = "containerId";

    dockerMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .setBody("true")
    );

    final var response = client.start(containerId).block();
    assertThat(response).isTrue();

    final RecordedRequest dockerRequest = dockerMockWebServer.takeRequest();
    assertThat(dockerRequest.getPath()).isEqualTo("/container/start?containerId=containerId");
  }

  @Test
  public void shouldStop() throws InterruptedException {
    final var containerId = "containerId";
    final var wait = Optional.<Integer>empty();

    dockerMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .setBody("true")
    );

    final var response = client.stop(containerId, wait).block();
    assertThat(response).isTrue();

    final RecordedRequest dockerRequest = dockerMockWebServer.takeRequest();
    assertThat(dockerRequest.getPath()).isEqualTo("/container/stop?containerId=containerId");
  }

  @Test
  public void shouldStopWait() throws InterruptedException {
    final var containerId = "containerId";
    final var wait = Optional.of(42);

    dockerMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .setBody("true")
    );

    final var response = client.stop(containerId, wait).block();
    assertThat(response).isTrue();

    final RecordedRequest dockerRequest = dockerMockWebServer.takeRequest();
    assertThat(dockerRequest.getPath()).isEqualTo("/container/stop?containerId=containerId&secondsToWaitBeforeKilling=42");
    assertThat(dockerRequest.getMethod()).isEqualTo("DELETE");
  }

  @Test
  public void shouldTail() throws InterruptedException {
    final var containerId = "containerId";
    final var lines = Optional.<Integer>empty();

    dockerMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .setBody("some logs")
    );

    final var response = client.tail(containerId, lines).block();
    assertThat(response).isEqualTo("some logs");

    final RecordedRequest dockerRequest = dockerMockWebServer.takeRequest();
    assertThat(dockerRequest.getPath()).isEqualTo("/container/tail?containerId=containerId");
  }

  @Test
  public void shouldTailLines() throws InterruptedException {
    final var containerId = "containerId";
    final var wait = Optional.of(42);

    dockerMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .setBody("some logs")
    );

    final var response = client.tail(containerId, wait).block();
    assertThat(response).isEqualTo("some logs");

    final RecordedRequest dockerRequest = dockerMockWebServer.takeRequest();
    assertThat(dockerRequest.getPath()).isEqualTo("/container/tail?containerId=containerId&lines=42");
  }


  @Test
  public void shouldPs() throws InterruptedException, JsonProcessingException {
    dockerMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(ImmutableList.of(DockerContainerTest.DOCKER_CONTAINER)))
    );

    final var response = client.ps().blockFirst();
    assertThat(response).isEqualTo(DockerContainerTest.DOCKER_CONTAINER);

    final RecordedRequest dockerRequest = dockerMockWebServer.takeRequest();
    assertThat(dockerRequest.getPath()).isEqualTo("/container");
  }


  @Test
  public void shouldInspect() throws InterruptedException, JsonProcessingException {
    final var containerId = "containerId";

    dockerMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(DockerContainerTest.DOCKER_CONTAINER))
    );

    final var response = client.inspect(containerId).block();
    assertThat(response).isEqualTo(DockerContainerTest.DOCKER_CONTAINER);

    final RecordedRequest dockerRequest = dockerMockWebServer.takeRequest();
    assertThat(dockerRequest.getPath()).isEqualTo("/container/inspect?containerId=containerId");
  }

  @Test
  public void shouldRm() throws InterruptedException {
    final var containerId = "containerId";

    dockerMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .setBody("true")
    );

    final var response = client.rm(containerId).block();
    assertThat(response).isTrue();

    final RecordedRequest dockerRequest = dockerMockWebServer.takeRequest();
    assertThat(dockerRequest.getPath()).isEqualTo("/container?containerId=containerId");
    assertThat(dockerRequest.getMethod()).isEqualTo("DELETE");
  }

}
