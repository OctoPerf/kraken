package com.kraken.runtime.server.rest;

import com.kraken.runtime.entity.task.ContainerTest;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.FlatContainerTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static com.kraken.tests.utils.TestUtils.shouldPassNPE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ContainerControllerTest extends RuntimeControllerTest {

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(ContainerController.class);
  }

  @Test
  public void shouldAttachLogs() {
    final var applicationId = "test";
    final var owner = userOwner().withApplicationId(applicationId);
    final var containerId = "containerId";
    final var containerName = "containerName";
    final var taskId = "taskId";
    given(service.attachLogs(owner, taskId, containerId, containerName))
        .willReturn(Mono.fromCallable(() -> null));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/container/logs/attach")
            .queryParam("taskId", taskId)
            .queryParam("containerName", containerName)
            .queryParam("containerId", containerId)
            .build())
        .header("ApplicationId", applicationId)
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk();

    verify(service).attachLogs(owner, taskId, containerId, containerName);
  }

  @Test
  public void shouldFailToAttachLogs() {
    final var applicationId = "applicationId"; // Should match [a-z0-9]*
    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/container/logs/attach")
            .queryParam("taskId", "taskId")
            .queryParam("containerName", "containerName")
            .queryParam("containerId", "containerId")
            .build())
        .header("ApplicationId", applicationId)
        .header("Authorization", "Bearer user-token")
        .body(BodyInserters.fromValue(ContainerTest.CONTAINER))
        .exchange()
        .expectStatus().is5xxServerError();
  }

  @Test
  public void shouldDetachLogs() {
    final var appId = "appid";
    final var owner = userOwner().withApplicationId(appId);
    final var id = "id";
    given(service.detachLogs(owner, id))
        .willReturn(Mono.fromCallable(() -> null));

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/container/logs/detach")
            .queryParam("id", "id")
            .build())
        .header("ApplicationId", appId)
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk();

    verify(service).detachLogs(owner, id);
  }

  @Test
  public void shouldSetStatus() {
    final var appId = "appid";
    final var owner = userOwner().withApplicationId(appId);
    final var containerId = "containerId";
    final var containerName = "containerName";
    final var taskId = "taskId";
    final var container = ContainerTest.CONTAINER;
    given(service.setStatus(owner, taskId, containerId, containerName, container.getStatus()))
        .willReturn(Mono.fromCallable(() -> null));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/container/status")
            .pathSegment(container.getStatus().toString())
            .queryParam("taskId", taskId)
            .queryParam("containerName", containerName)
            .queryParam("containerId", containerId)
            .build())
        .header("ApplicationId", appId)
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk();

    verify(service).setStatus(owner, taskId, containerId, containerName, container.getStatus());
  }

  @Test
  public void shouldFind() {
    final var appId = "appid";
    final var owner = userOwner().withApplicationId(appId);
    final var containerName = "containerName";
    final var taskId = "taskId";
    final var container = FlatContainerTest.CONTAINER;
    given(service.find(owner, taskId, containerName))
        .willReturn(Mono.just(container));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/container/find")
            .queryParam("taskId", taskId)
            .queryParam("containerName", containerName)
            .build())
        .header("ApplicationId", appId)
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectBody(FlatContainer.class)
        .isEqualTo(container);

    verify(service).find(owner, taskId, containerName);
  }
}