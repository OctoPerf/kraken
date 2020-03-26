package com.kraken.runtime.server.rest;

import com.google.common.base.Charsets;
import com.kraken.runtime.entity.log.LogTest;
import com.kraken.runtime.logs.LogsService;
import com.kraken.tools.sse.SSEService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Optional;

import static com.kraken.test.utils.TestUtils.shouldPassNPE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {LogsController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class LogsControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  LogsService logsService;

  @MockBean
  SSEService sse;

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(LogsController.class);
  }

  @Test
  public void shouldWatch() {
    final var applicationId = "test";
    final var logFlux = Flux.just(LogTest.LOG);
    final var eventsFlux = Flux.just(ServerSentEvent.builder(LogTest.LOG).build(), ServerSentEvent.builder(LogTest.LOG).build());
    given(logsService.listen(applicationId)).willReturn(logFlux);
    given(sse.keepAlive(logFlux)).willReturn(eventsFlux);

    final var result = webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/logs/watch").pathSegment(applicationId).build())
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
        .expectBody()
        .returnResult();
    final var body = new String(Optional.ofNullable(result.getResponseBody()).orElse(new byte[0]), Charsets.UTF_8);
    Assertions.assertThat(body).isEqualTo("data:{\"applicationId\":\"applicationId\",\"id\":\"id\",\"type\":\"CONTAINER\",\"text\":\"text\",\"status\":\"RUNNING\"}\n" +
        "\n" +
        "data:{\"applicationId\":\"applicationId\",\"id\":\"id\",\"type\":\"CONTAINER\",\"text\":\"text\",\"status\":\"RUNNING\"}\n" +
        "\n");
  }

  @Test
  public void shouldFailToWatch() {
    final var applicationId = "applicationId"; // Should match [a-z0-9]*
    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/logs/watch").pathSegment(applicationId).build())
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .exchange()
        .expectStatus().is5xxServerError();
  }

}