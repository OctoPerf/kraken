package com.kraken.runtime.server.rest;

import com.google.common.base.Charsets;
import com.kraken.runtime.entity.log.LogTest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.Optional;

import static com.kraken.tests.utils.TestUtils.shouldPassNPE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class LogsControllerTest extends RuntimeControllerTest {

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(LogsController.class);
  }

  @Test
  public void shouldWatch() {
    final var logFlux = Flux.just(LogTest.LOG);
    final var eventsFlux = Flux.just(ServerSentEvent.builder(LogTest.LOG).build(), ServerSentEvent.builder(LogTest.LOG).build());
    given(logsService.listen(userOwner())).willReturn(logFlux);
    given(sse.keepAlive(logFlux)).willReturn(eventsFlux);

    final var result = webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/logs/watch").build())
        .header("Authorization", "Bearer user-token")
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
        .expectBody()
        .returnResult();
    final var body = new String(Optional.ofNullable(result.getResponseBody()).orElse(new byte[0]), Charsets.UTF_8);
    Assertions.assertThat(body).isEqualTo("data:{\"owner\":{\"userId\":\"userId\",\"applicationId\":\"applicationId\",\"roles\":[\"USER\"],\"type\":\"USER\"},\"id\":\"id\",\"type\":\"CONTAINER\",\"text\":\"text\",\"status\":\"RUNNING\"}\n" +
        "\n" +
        "data:{\"owner\":{\"userId\":\"userId\",\"applicationId\":\"applicationId\",\"roles\":[\"USER\"],\"type\":\"USER\"},\"id\":\"id\",\"type\":\"CONTAINER\",\"text\":\"text\",\"status\":\"RUNNING\"}\n" +
        "\n");
  }

  @Test
  public void shouldFailToWatch() {
    final var applicationId = "applicationId"; // Should match [a-z0-9]*
    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/logs/watch").build())
        .header("ApplicationId", applicationId)
        .header("Authorization", "Bearer user-token")
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .exchange()
        .expectStatus().is5xxServerError();
  }

}