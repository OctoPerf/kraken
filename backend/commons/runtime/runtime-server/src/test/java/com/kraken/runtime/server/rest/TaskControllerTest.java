package com.kraken.runtime.server.rest;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.backend.api.TaskService;
import com.kraken.runtime.context.api.ExecutionContextService;
import com.kraken.runtime.context.entity.CancelContextTest;
import com.kraken.runtime.context.entity.ExecutionContextTest;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentTest;
import com.kraken.runtime.entity.task.Task;
import com.kraken.runtime.entity.task.TaskTest;
import com.kraken.runtime.event.TaskCancelledEvent;
import com.kraken.runtime.event.TaskExecutedEvent;
import com.kraken.runtime.server.service.TaskListService;
import com.kraken.test.utils.TestUtils;
import com.kraken.tools.event.bus.EventBus;
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

import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_DESCRIPTION;
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
  ExecutionContextService contextService;

  @MockBean
  EventBus eventBus;

  @MockBean
  SSEService sse;

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(TaskController.class);
  }

  @Test
  public void shouldRun() {
    final var env = ExecutionEnvironmentTest.EXECUTION_ENVIRONMENT;
    final var context = ExecutionContextTest.EXECUTION_CONTEXT;
    final var applicationId = context.getApplicationId();
    final var taskId = context.getTaskId();

    given(contextService.newExecuteContext(applicationId, env)).willReturn(Mono.just(context));
    given(taskService.execute(context))
        .willReturn(Mono.just(context));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/task")
            .build())
        .header("ApplicationId", applicationId)
        .body(BodyInserters.fromValue(env))
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo(taskId);

    verify(eventBus).publish(TaskExecutedEvent.builder()
        .context(context)
        .build());
  }

  @Test
  public void shouldFailToRun() {
    final var applicationId = "applicationId"; // Should match [a-z0-9]*
    final var env = ImmutableMap.of(KRAKEN_DESCRIPTION.name(), "description");
    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/task")
            .build())
        .header("ApplicationId", applicationId)
        .body(BodyInserters.fromValue(env))
        .exchange()
        .expectStatus().is5xxServerError();
  }

  @Test
  public void shouldCancel() {
    final var context = CancelContextTest.CANCEL_CONTEXT;
    final var applicationId = context.getApplicationId();
    final var taskId = context.getTaskId();
    final var taskType = context.getTaskType();

    given(contextService.newCancelContext(applicationId, taskId, taskType)).willReturn(Mono.just(context));

    given(taskService.cancel(context))
        .willReturn(Mono.just(context));

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/task/cancel")
            .pathSegment(taskType.toString())
            .queryParam("taskId", taskId)
            .build())
        .header("ApplicationId", applicationId)
        .exchange()
        .expectStatus().isOk();

    verify(taskService).cancel(context);
    verify(eventBus).publish(TaskCancelledEvent.builder().context(context)
        .build());
  }

  @Test
  public void shouldRemove() {
    final var context = CancelContextTest.CANCEL_CONTEXT;
    final var applicationId = context.getApplicationId();
    final var taskId = context.getTaskId();
    final var taskType = context.getTaskType();

    given(contextService.newCancelContext(applicationId, taskId, taskType)).willReturn(Mono.just(context));

    given(taskService.remove(context))
        .willReturn(Mono.just(context));

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/task/remove")
            .pathSegment(taskType.toString())
            .queryParam("taskId", taskId)
            .build())
        .header("ApplicationId", applicationId)
        .exchange()
        .expectStatus().isOk();

    verify(taskService).remove(context);
  }

  @Test
  public void shouldFailToCancel() {
    final var applicationId = "applicationId"; // Should match [a-z0-9]*
    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/task/cancel")
            .pathSegment("GATLING_RUN")
            .queryParam("taskId", "taskId")
            .build())
        .header("ApplicationId", applicationId)
        .exchange()
        .expectStatus().is5xxServerError();
  }

  @Test
  public void shouldList() {
    final var tasksFlux = Flux.just(TaskTest.TASK);
    given(taskListService.list(Optional.of("app")))
        .willReturn(tasksFlux);

    webTestClient.get()
        .uri("/task/list")
        .header("ApplicationId", "app")
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
    given(taskListService.watch(Optional.of("app"))).willReturn(tasksFlux);
    given(sse.keepAlive(tasksFlux)).willReturn(eventsFlux);

    final var result = webTestClient.get()
        .uri("/task/watch/app")
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
        .expectBody()
        .returnResult();
    final var body = new String(Optional.ofNullable(result.getResponseBody()).orElse(new byte[0]), Charsets.UTF_8);
    assertThat(body).isEqualTo("data:[{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"GATLING_RUN\",\"containers\":[],\"expectedCount\":2,\"description\":\"description\",\"applicationId\":\"app\"},{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"GATLING_RUN\",\"containers\":[],\"expectedCount\":2,\"description\":\"description\",\"applicationId\":\"app\"}]\n" +
        "\n" +
        "data:[{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"GATLING_RUN\",\"containers\":[],\"expectedCount\":2,\"description\":\"description\",\"applicationId\":\"app\"},{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"GATLING_RUN\",\"containers\":[],\"expectedCount\":2,\"description\":\"description\",\"applicationId\":\"app\"}]\n" +
        "\n");
  }

}