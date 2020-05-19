package com.kraken.runtime.server.rest;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.kraken.runtime.context.entity.CancelContextTest;
import com.kraken.runtime.context.entity.ExecutionContextTest;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentTest;
import com.kraken.runtime.entity.host.HostTest;
import com.kraken.runtime.entity.task.Task;
import com.kraken.runtime.entity.task.TaskTest;
import com.kraken.runtime.event.*;
import com.kraken.tests.utils.TestUtils;
import com.kraken.tools.sse.SSEWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class TaskControllerTest extends RuntimeControllerTest {

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(TaskController.class);
  }

  @Test
  public void shouldRun() {
    final var env = ExecutionEnvironmentTest.EXECUTION_ENVIRONMENT;
    final var hosts = ExecutionEnvironmentTest.EXECUTION_ENVIRONMENT.getHostIds().stream().map(hostId -> HostTest.HOST.toBuilder().owner(userOwner()).id(hostId).build()).collect(Collectors.toUnmodifiableList());
    final var context = ExecutionContextTest.EXECUTION_CONTEXT;
    final var taskId = context.getTaskId();

    given(contextService.newExecuteContext(userOwner(), env)).willReturn(Mono.just(context));
    given(taskService.execute(context)).willReturn(Mono.just(context));
    given(hostService.list(userOwner())).willReturn(Flux.fromIterable(hosts));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/task").build())
        .header("Authorization", "Bearer user-token")
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
  public void shouldRunIllegal() {
    final var env = ExecutionEnvironmentTest.EXECUTION_ENVIRONMENT;
    final var hosts = ImmutableList.of(HostTest.HOST);

    given(hostService.list(userOwner())).willReturn(Flux.fromIterable(hosts));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/task").build())
        .header("Authorization", "Bearer user-token")
        .body(BodyInserters.fromValue(env))
        .exchange()
        .expectStatus().is5xxServerError();
  }

  @Test
  public void shouldFailToRun() {
    final var applicationId = "applicationId"; // Should match [a-z0-9]*
    final var env = ExecutionEnvironmentTest.EXECUTION_ENVIRONMENT;
    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/task").build())
        .header("Authorization", "Bearer user-token")
        .header("ApplicationId", applicationId)
        .body(BodyInserters.fromValue(env))
        .exchange()
        .expectStatus().is5xxServerError();
  }

  @Test
  public void shouldCancel() {
    final var context = CancelContextTest.CANCEL_CONTEXT;
    final var taskId = context.getTaskId();
    final var taskType = context.getTaskType();

    given(contextService.newCancelContext(userOwner(), taskId, taskType)).willReturn(Mono.just(context));

    given(taskService.cancel(context))
        .willReturn(Mono.just(context));

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/task/cancel")
            .pathSegment(taskType.toString())
            .queryParam("taskId", taskId)
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk();

    verify(taskService).cancel(context);
    verify(eventBus).publish(TaskCancelledEvent.builder().context(context)
        .build());
  }

  @Test
  public void shouldRemove() {
    final var context = CancelContextTest.CANCEL_CONTEXT;
    final var taskId = context.getTaskId();
    final var taskType = context.getTaskType();

    given(contextService.newCancelContext(userOwner(), taskId, taskType)).willReturn(Mono.just(context));

    given(taskService.remove(context))
        .willReturn(Mono.just(context));

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/task/remove")
            .pathSegment(taskType.toString())
            .queryParam("taskId", taskId)
            .build())
        .header("Authorization", "Bearer user-token")
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
        .header("Authorization", "Bearer user-token")
        .header("ApplicationId", applicationId)
        .exchange()
        .expectStatus().is5xxServerError();
  }

  @Test
  public void shouldList() {
    final var tasksFlux = Flux.just(TaskTest.TASK);
    given(taskListService.list(userOwner()))
        .willReturn(tasksFlux);

    webTestClient.get()
        .uri("/task/list")
        .header("Authorization", "Bearer user-token")
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
    given(taskListService.watch(userOwner())).willReturn(tasksFlux);
    given(sse.keepAlive(tasksFlux)).willReturn(eventsFlux);

    final var result = webTestClient.get()
        .uri("/task/watch")
        .header("Authorization", "Bearer user-token")
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
        .expectBody()
        .returnResult();
    final var body = new String(Optional.ofNullable(result.getResponseBody()).orElse(new byte[0]), Charsets.UTF_8);
    assertThat(body).isEqualTo("data:[{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"GATLING_RUN\",\"containers\":[],\"expectedCount\":2,\"description\":\"description\",\"owner\":{\"userId\":\"userId\",\"applicationId\":\"applicationId\",\"roles\":[\"USER\"],\"type\":\"USER\"}},{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"GATLING_RUN\",\"containers\":[],\"expectedCount\":2,\"description\":\"description\",\"owner\":{\"userId\":\"userId\",\"applicationId\":\"applicationId\",\"roles\":[\"USER\"],\"type\":\"USER\"}}]\n" +
        "\n" +
        "data:[{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"GATLING_RUN\",\"containers\":[],\"expectedCount\":2,\"description\":\"description\",\"owner\":{\"userId\":\"userId\",\"applicationId\":\"applicationId\",\"roles\":[\"USER\"],\"type\":\"USER\"}},{\"id\":\"id\",\"startDate\":42,\"status\":\"STARTING\",\"type\":\"GATLING_RUN\",\"containers\":[],\"expectedCount\":2,\"description\":\"description\",\"owner\":{\"userId\":\"userId\",\"applicationId\":\"applicationId\",\"roles\":[\"USER\"],\"type\":\"USER\"}}]\n" +
        "\n");
  }

  @Test
  public void shouldEventsFail() {
    webTestClient.get()
        .uri("/task/events")
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().is4xxClientError();
  }

  @Test
  public void shouldEvents() {
    given(eventBus.of(TaskExecutedEvent.class)).willReturn(Flux.just(TaskExecutedEventTest.TASK_EXECUTED_EVENT));
    given(eventBus.of(TaskCreatedEvent.class)).willReturn(Flux.just(TaskCreatedEventTest.TASK_CREATED_EVENT));
    given(eventBus.of(TaskStatusUpdatedEvent.class)).willReturn(Flux.just(TaskStatusUpdatedEventTest.TASK_STATUS_UPDATED_EVENT));
    given(eventBus.of(TaskCancelledEvent.class)).willReturn(Flux.just(TaskCancelledEventTest.TASK_CANCELLED_EVENT));
    given(eventBus.of(TaskRemovedEvent.class)).willReturn(Flux.just(TaskRemovedEventTest.TASK_REMOVED_EVENT));
    final var wrapped = Flux.just(SSEWrapper.builder().type("TaskExecutedEvent").value(TaskExecutedEventTest.TASK_EXECUTED_EVENT).build());
    given(sse.merge(any())).willReturn(wrapped);
    final Flux<ServerSentEvent<SSEWrapper>> eventsFlux = wrapped.map(sseWrapper -> ServerSentEvent.<SSEWrapper>builder().data(sseWrapper).build());
    given(sse.keepAlive(wrapped)).willReturn(eventsFlux);

    final var result = webTestClient.get()
        .uri("/task/events")
        .header("Authorization", "Bearer api-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
        .expectBody()
        .returnResult();

    final var body = new String(Optional.ofNullable(result.getResponseBody()).orElse(new byte[0]), Charsets.UTF_8);
    assertThat(body).isEqualTo("data:{\"type\":\"TaskExecutedEvent\",\"value\":{\"context\":{\"owner\":{\"userId\":\"userId\",\"applicationId\":\"applicationId\",\"roles\":[\"USER\"],\"type\":\"USER\"},\"taskId\":\"taskId\",\"taskType\":\"GATLING_RUN\",\"description\":\"description\",\"templates\":{\"hostId\":\"template\"}}}}\n" +
        "\n");
  }
}