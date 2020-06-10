package com.octoperf.kraken.runtime.event.client.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.runtime.client.api.RuntimeClientProperties;
import com.octoperf.kraken.runtime.event.TaskCreatedEvent;
import com.octoperf.kraken.runtime.event.TaskCreatedEventTest;
import com.octoperf.kraken.runtime.event.TaskStatusUpdatedEvent;
import com.octoperf.kraken.runtime.event.TaskStatusUpdatedEventTest;
import com.octoperf.kraken.runtime.event.client.api.RuntimeEventClient;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.api.ExchangeFilterFactory;
import com.octoperf.kraken.tools.sse.SSEWrapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class WebRuntimeEventClientTest {

  private MockWebServer server;
  private RuntimeEventClient client;

  @Autowired
  List<ExchangeFilterFactory> filterFactories;

  @Autowired
  ObjectMapper objectMapper;
  @MockBean
  RuntimeClientProperties properties;

  @BeforeEach
  public void before() {
    server = new MockWebServer();
    final String baseUrl = server.url("/").toString();
    when(properties.getUrl()).thenReturn(baseUrl);
    client = (RuntimeEventClient) new WebRuntimeEventClientBuilder(filterFactories, properties, objectMapper).mode(AuthenticationMode.NOOP).build().block();
  }

  @AfterEach
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  public void shouldWatch() throws InterruptedException, JsonProcessingException {
    final var body = ":keep alive\n" +
        "\n" +
        "data:" + objectMapper.writeValueAsString(SSEWrapper.builder().type(TaskCreatedEvent.class.getSimpleName()).value(TaskCreatedEventTest.TASK_CREATED_EVENT).build()) + "\n" +
        "\n" +
        ":keep alive\n" +
        "\n" +
        "data:" + objectMapper.writeValueAsString(SSEWrapper.builder().type(TaskStatusUpdatedEvent.class.getSimpleName()).value(TaskStatusUpdatedEventTest.TASK_STATUS_UPDATED_EVENT).build()) + "\n" +
        "\n" +
        ":keep alive\n" +
        "\n";

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
            .setBody(body));

    final var response = client.events().collectList().block();
    assertThat(response).isNotNull();
    assertThat(response.size()).isEqualTo(2);
    assertThat(response.get(0)).isEqualTo(TaskCreatedEventTest.TASK_CREATED_EVENT);
    assertThat(response.get(1)).isEqualTo(TaskStatusUpdatedEventTest.TASK_STATUS_UPDATED_EVENT);

    final var request = server.takeRequest();
    assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.TEXT_EVENT_STREAM_VALUE);
    assertThat(request.getPath()).isEqualTo("/task/events");
  }
}
