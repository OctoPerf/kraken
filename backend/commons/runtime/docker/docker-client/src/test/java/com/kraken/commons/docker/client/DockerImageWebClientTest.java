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

public class DockerImageWebClientTest {

  private ObjectMapper mapper;
  private MockWebServer dockerMockWebServer;
  private DockerImageClient client;

  @Before
  public void before() {
    dockerMockWebServer = new MockWebServer();
    mapper = new ObjectMapper();
    client = new DockerImageWebClient(WebClient.create(dockerMockWebServer.url("/").toString()));
  }

  @After
  public void tearDown() throws IOException {
    dockerMockWebServer.shutdown();
  }

  @Test
  public void shouldImages() throws InterruptedException, JsonProcessingException {
    dockerMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(ImmutableList.of(DockerImageTest.DOCKER_IMAGE)))
    );

    final var response = client.images().blockFirst();
    assertThat(response).isEqualTo(DockerImageTest.DOCKER_IMAGE);

    final RecordedRequest dockerRequest = dockerMockWebServer.takeRequest();
    assertThat(dockerRequest.getPath()).isEqualTo("/image");
  }

  @Test
  public void shouldRmi() throws InterruptedException {
    final var imageId = "imageId";

    dockerMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .setBody("true")
    );

    final var response = client.rmi(imageId).block();
    assertThat(response).isTrue();

    final RecordedRequest dockerRequest = dockerMockWebServer.takeRequest();
    assertThat(dockerRequest.getPath()).isEqualTo("/image?imageId=imageId");
    assertThat(dockerRequest.getMethod()).isEqualTo("DELETE");
  }
}
