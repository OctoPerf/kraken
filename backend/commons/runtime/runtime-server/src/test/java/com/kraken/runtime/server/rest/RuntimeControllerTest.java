package com.kraken.runtime.server.rest;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.LogTest;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskTest;
import com.kraken.runtime.logs.LogsService;
import com.kraken.tools.sse.SSEService;
import com.kraken.tools.sse.SSEWrapper;
import com.kraken.tools.sse.SSEWrapperTest;
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

import java.util.List;
import java.util.Optional;

import static com.kraken.test.utils.TestUtils.shouldPassNPE;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {RuntimeController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class RuntimeControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  LogsService logsService;

  @MockBean
  TaskService taskService;

  @MockBean
  SSEService sse;

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(RuntimeController.class);
  }

  @Test
  public void shouldAttachLogs() {
    final var applicationId = "applicationId";
    final var logFlux = Flux.just(LogTest.LOG);
    final Flux<SSEWrapper> wrapperFlux = Flux.just(SSEWrapperTest.WRAPPER_STRING, SSEWrapperTest.WRAPPER_INT);
    final var eventsFlux = Flux.just(ServerSentEvent.builder(SSEWrapperTest.WRAPPER_STRING).build(), ServerSentEvent.builder(SSEWrapperTest.WRAPPER_INT).build());
    final Flux<List<Task>> tasksFlux = Flux.just(ImmutableList.of(TaskTest.TASK));
    given(logsService.listen(applicationId))
        .willReturn(logFlux);
    given(taskService.watch()).willReturn(tasksFlux);
    given(sse.merge("LOG", logFlux, "TASKS", tasksFlux)).willReturn(wrapperFlux);
    given(sse.keepAlive(wrapperFlux)).willReturn(eventsFlux);

    final var result = webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/runtime/watch").pathSegment(applicationId).build())
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
        .expectBody()
        .returnResult();
    final var body = new String(Optional.ofNullable(result.getResponseBody()).orElse(new byte[0]), Charsets.UTF_8);
    Assertions.assertThat(body).isEqualTo("data:{\"type\":\"String\",\"value\":\"value\"}\n" +
        "\n" +
        "data:{\"type\":\"Integer\",\"value\":42}\n" +
        "\n");
  }

}