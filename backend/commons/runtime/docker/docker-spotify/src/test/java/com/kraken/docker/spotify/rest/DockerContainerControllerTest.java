package com.kraken.docker.spotify.rest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.command.client.CommandClient;
import com.kraken.command.entity.Command;
import com.kraken.docker.entity.DockerContainerTest;
import com.kraken.docker.spotify.DockerService;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.kraken.test.utils.TestUtils.shouldPassNPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {DockerContainerController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class DockerContainerControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  DockerService service;

  @MockBean
  CommandClient commandClient;

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(DockerContainerController.class);
  }

  @Test
  public void shouldRunContainer() {
    given(service.run("name", "config"))
        .willReturn(Mono.just("started"));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/container/run")
            .queryParam("name", "name")
            .build())
        .body(BodyInserters.fromObject("config"))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo("started");
  }

  @Test
  public void shouldStartContainer() {
    final var containerId = "container-id/test";

    given(service.start(containerId))
        .willReturn(Mono.just(true));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/container/start")
            .queryParam("containerId", containerId)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody()
        .jsonPath("$")
        .isEqualTo("true");
  }

  @Test
  public void shouldStopContainer() {
    final var containerId = "container-id.test";
    given(service.stop(containerId, 10))
        .willReturn(Mono.just(true));

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/container/stop")
            .queryParam("containerId", containerId)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody()
        .jsonPath("$")
        .isEqualTo("true");
  }

  @Test
  public void shouldReturnLogs() {
    final var commandId = "commandId";
    final var containerId = "containerId";
    final var applicationId = "app";

    given(commandClient.execute(any()))
        .willReturn(Mono.just(commandId));

    final Command command = Command.builder()
        .id("")
        .applicationId(applicationId)
        .command(ImmutableList.of("docker", "logs", "-f", containerId))
        .environment(ImmutableMap.of())
        .path("")
        .onCancel(ImmutableList.of())
        .build();

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/container/logs")
            .queryParam("containerId", containerId)
            .build())
        .header("ApplicationId", applicationId)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo(commandId);

    verify(commandClient).execute(command);
  }

  @Test
  public void shouldReturnTail() {
    final var containerId = "containerId";

    given(service.tail("containerId", 100))
        .willReturn(Mono.just("log"));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/container/tail")
            .queryParam(containerId, "containerId")
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo("log");
  }

  @Test
  public void shouldReturnTailCustomLines() {
    final var containerId = "containerId";

    given(service.tail(containerId, 42))
        .willReturn(Mono.just("log"));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/container/tail")
            .queryParam("containerId", containerId)
            .queryParam("lines", 42)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo("log");
  }

  @Test
  public void shouldInspect() {
    final var containerId = "containerId";

    given(service.inspect(containerId))
        .willReturn(Mono.just(DockerContainerTest.DOCKER_CONTAINER));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/container/inspect")
            .queryParam("containerId", containerId)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody()
        .jsonPath("$.id")
        .isEqualTo(DockerContainerTest.DOCKER_CONTAINER.getId());
  }

  @Test
  public void shouldPs() {
    given(service.ps())
        .willReturn(Flux.just(DockerContainerTest.DOCKER_CONTAINER));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/container/ps")
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody()
        .jsonPath("$[0].id")
        .isEqualTo(DockerContainerTest.DOCKER_CONTAINER.getId());
  }

  @Test
  public void shouldRm() {
    final var containerId = "containerId";
    
    given(service.rm(containerId))
        .willReturn(Mono.just(true));

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/container")
            .queryParam("containerId", containerId)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody()
        .jsonPath("$")
        .isEqualTo("true");
  }

}
