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
import org.springframework.http.MediaType;
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
    given(service.attachLogs(applicationId, ContainerTest.CONTAINER))
        .willReturn(Mono.fromCallable(() -> null));

    webTestClient.post()
        .uri("/container/logs/attach")
        .header("ApplicationId", applicationId)
        .body(BodyInserters.fromObject(ContainerTest.CONTAINER))
        .exchange()
        .expectStatus().isOk();

    verify(service).attachLogs(applicationId, ContainerTest.CONTAINER);
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
    given(service.detachLogs(ContainerTest.CONTAINER))
        .willReturn(Mono.fromCallable(() -> null));

    webTestClient.post()
        .uri("/container/logs/detach")
        .body(BodyInserters.fromObject(ContainerTest.CONTAINER))
        .exchange()
        .expectStatus().isOk();

    verify(service).detachLogs(ContainerTest.CONTAINER);
  }

  @Test
  public void shouldSetStatus() {
    final var container = ContainerTest.CONTAINER;
    given(service.setStatus(container.getContainerId(), container.getStatus()))
        .willReturn(Mono.just(container));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/container/status")
            .pathSegment(container.getStatus().toString())
            .queryParam("containerId", container.getContainerId())
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody(Container.class)
        .isEqualTo(container);

    verify(service).setStatus(container.getContainerId(), container.getStatus());
  }

}