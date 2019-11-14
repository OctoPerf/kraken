package com.kraken.runtime.server.rest;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskTest;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.server.service.ResultUpdater;
import com.kraken.runtime.server.service.TaskListService;
import com.kraken.test.utils.TestUtils;
import com.kraken.tools.sse.SSEService;
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
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {TaskController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class TaskControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  TaskService taskService;

  @MockBean
  TaskListService taskListService;

  @MockBean
  ResultUpdater updater;

  @MockBean
  SSEService sse;

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(TaskController.class);
  }

  @Test
  public void shouldRun() {
    final var applicationId = "test";
    final var env = ImmutableMap.<String, String>of("KRAKEN_DESCRIPTION", "description");
    final var taskId = "taskId";
    given(taskService.hostsCount())
        .willReturn(Mono.just(42));
    given(taskService.execute(applicationId, TaskType.RUN, 1, env))
        .willReturn(Mono.just(taskId));
    given(updater.taskExecuted(taskId, TaskType.RUN, env)).willReturn(Mono.just(taskId));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/task")
            .pathSegment(TaskType.RUN.toString())
            .pathSegment("1")
            .build())
        .header("ApplicationId", applicationId)
        .body(BodyInserters.fromObject(env))
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo(taskId);
  }

  @Test
  public void shouldFailToRun() {
    final var applicationId = "applicationId"; // Should match [a-z0-9]*
    final var env = ImmutableMap.<String, String>of("KRAKEN_DESCRIPTION", "description");
    given(taskService.hostsCount())
        .willReturn(Mono.just(42));
    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/task")
            .pathSegment(TaskType.RUN.toString())
            .pathSegment("1")
            .queryParam("description", "description")
            .build())
        .header("ApplicationId", applicationId)
        .body(BodyInserters.fromObject(env))
        .exchange()
        .expectStatus().is5xxServerError();
  }

  @Test
  public void shouldFailToRunCapacity() {
    final var applicationId = "test";
    final var env = ImmutableMap.<String, String>of("KRAKEN_DESCRIPTION", "description");
    final var taskId = "taskId";
    given(taskService.hostsCount())
        .willReturn(Mono.just(2));
    given(taskService.execute(applicationId, TaskType.RUN, 3, env))
        .willReturn(Mono.just(taskId));
    given(updater.taskExecuted(taskId, TaskType.RUN, env)).willReturn(Mono.just(taskId));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/task")
            .pathSegment(TaskType.RUN.toString())
            .pathSegment("3")
            .build())
        .header("ApplicationId", applicationId)
        .body(BodyInserters.fromObject(env))
        .exchange()
        .expectStatus().is5xxServerError()
        .expectBody(String.class)
        .value(s -> assertThat(s).contains("Insufficient capacity (2) to run 3 replicas!"));
  }

  @Test
  public void shouldCancel() {
    final var applicationId = "test";
    final var taskId = "taskId";
    final var taskType = TaskType.RUN;
    given(taskService.cancel(applicationId, taskId, taskType))
        .willReturn(Mono.just(taskId));
    given(updater.taskCanceled(taskId))
        .willReturn(Mono.just(taskId));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/task/cancel")
            .pathSegment(taskType.toString())
            .queryParam("taskId", taskId)
            .build())
        .header("ApplicationId", applicationId)
        .exchange()
        .expectStatus().isOk();

    verify(taskService).cancel(applicationId, taskId, taskType);
    verify(updater).taskCanceled(taskId);
  }

  @Test
  public void shouldFailToCancel() {
    final var applicationId = "applicationId"; // Should match [a-z0-9]*
    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/task/cancel")
            .pathSegment("RUN")
            .queryParam("taskId", "taskId")
            .build())
        .header("ApplicationId", applicationId)
        .body(BodyInserters.fromObject(TaskTest.TASK))
        .exchange()
        .expectStatus().is5xxServerError();
  }

  @Test
  public void shouldList() {
    final var tasksFlux = Flux.just(TaskTest.TASK);
    given(taskListService.list())
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
    given(taskListService.watch()).willReturn(tasksFlux);
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
    assertThat(body).isEqualTo("data:[{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"RUN\",\"containers\":[],\"description\":\"description\"},{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"RUN\",\"containers\":[],\"description\":\"description\"}]\n" +
        "\n" +
        "data:[{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"RUN\",\"containers\":[],\"description\":\"description\"},{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"RUN\",\"containers\":[],\"description\":\"description\"}]\n" +
        "\n");
  }

}