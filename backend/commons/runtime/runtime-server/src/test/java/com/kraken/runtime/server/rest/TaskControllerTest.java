package com.kraken.runtime.server.rest;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.api.ContainerService;
import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.*;
import com.kraken.runtime.server.service.ResultUpdater;
import com.kraken.tools.sse.SSEService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static com.kraken.test.utils.TestUtils.shouldPassNPE;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {TaskController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class TaskControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  TaskService service;

  @MockBean
  ResultUpdater updater;

  @MockBean
  SSEService sse;

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(TaskController.class);
  }

  @Test
  public void shouldRun() {
    final var applicationId = "applicationId";
    final var env = ImmutableMap.<String, String>of();
    final var taskId = "taskId";
    given(service.execute(applicationId, TaskType.RUN, "Foo", env))
        .willReturn(Mono.just(taskId));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/task").pathSegment(TaskType.RUN.toString()).build())
        .header("ApplicationId", applicationId)
        .body(BodyInserters.fromObject(env))
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo(taskId);
  }

  @Test
  public void shouldCancel() {
    final var applicationId = "applicationId";
    final var task = TaskTest.TASK;
    given(service.cancel(applicationId, task))
        .willReturn(Mono.fromCallable(() -> null));

    webTestClient.post()
        .uri("/task/cancel")
        .header("ApplicationId", applicationId)
        .body(BodyInserters.fromObject(task))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void shouldList() {
    final var tasksFlux = Flux.just(TaskTest.TASK);
    given(service.list())
        .willReturn(tasksFlux);

    webTestClient.get()
        .uri("/task/list")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Task.class)
        .contains(TaskTest.TASK);
  }

  @Test
  public void shouldWatch() {
    final List<Task> list = ImmutableList.of(TaskTest.TASK, TaskTest.TASK);
    final Flux<List<Task>> tasksFlux = Flux.just(list, list);
    final Flux<ServerSentEvent<List<Task>>> eventsFlux = Flux.just(ServerSentEvent.builder(list).build(), ServerSentEvent.builder(list).build());
    given(service.watch()).willReturn(tasksFlux);
    given(sse.keepAlive(tasksFlux)).willReturn(eventsFlux);

    final var result = webTestClient.get()
        .uri("/task/watch")
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
        .expectBody()
        .returnResult();
    final var body = new String(Optional.ofNullable(result.getResponseBody()).orElse(new byte[0]), Charsets.UTF_8);
    Assertions.assertThat(body).isEqualTo("data:[{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"RUN\",\"containers\":[],\"description\":\"description\"},{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"RUN\",\"containers\":[],\"description\":\"description\"}]\n" +
        "\n" +
        "data:[{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"RUN\",\"containers\":[],\"description\":\"description\"},{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"RUN\",\"containers\":[],\"description\":\"description\"}]\n" +
        "\n");
  }

}