package com.kraken.runtime.server.rest;

import com.kraken.runtime.api.ContainerService;
import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static com.kraken.test.utils.TestUtils.shouldPassNPE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {ContainerController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class ContainerControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  ContainerService service;

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(ContainerController.class);
  }

  @Test
  public void shouldAttachLogs() {
    final var applicationId = "test";
    final var containerId = "containerId";
    final var taskId = "containerId";
    given(service.attachLogs(applicationId, taskId, containerId))
        .willReturn(Mono.fromCallable(() -> null));

    webTestClient.post()
        .uri("/container/logs/attach")
        .header("ApplicationId", applicationId)
        .exchange()
        .expectStatus().isOk();

    verify(service).attachLogs(applicationId, taskId, containerId);
  }

  @Test
  public void shouldFailToAttachLogs() {
    final var applicationId = "applicationId"; // Should match [a-z0-9]*
    webTestClient.post()
        .uri("/container/logs/attach")
        .header("ApplicationId", applicationId)
        .body(BodyInserters.fromObject(ContainerTest.CONTAINER))
        .exchange()
        .expectStatus().is5xxServerError();
  }

  @Test
  public void shouldDetachLogs() {
    final var containerId = "containerId";
    final var taskId = "containerId";
    given(service.detachLogs(taskId, containerId))
        .willReturn(Mono.fromCallable(() -> null));

    webTestClient.post()
        .uri("/container/logs/detach")
        .body(BodyInserters.fromObject(ContainerTest.CONTAINER))
        .exchange()
        .expectStatus().isOk();

    verify(service).detachLogs(taskId, containerId);
  }

  @Test
  public void shouldSetStatus() {
    final var containerId = "containerId";
    final var taskId = "containerId";
    final var container = ContainerTest.CONTAINER;
    given(service.setStatus(taskId, containerId, container.getStatus()))
        .willReturn(Mono.just(container));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/container/status")
            .pathSegment(container.getStatus().toString())
            .queryParam("taskId", taskId)
            .queryParam("containerId", containerId)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody(Container.class)
        .isEqualTo(container);

    verify(service).setStatus(taskId, containerId, container.getStatus());
  }

}