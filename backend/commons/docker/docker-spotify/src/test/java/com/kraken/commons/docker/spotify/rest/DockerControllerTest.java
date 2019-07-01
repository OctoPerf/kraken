package com.kraken.commons.docker.spotify.rest;

import com.kraken.commons.docker.entity.DockerContainerTest;
import com.kraken.commons.docker.entity.DockerImageTest;
import com.kraken.commons.docker.spotify.DockerService;
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
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {DockerController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class DockerControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  DockerService service;

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(DockerController.class);
  }

  @Test
  public void shouldPullImage() {
    given(service.pull("app", "image"))
        .willReturn(Mono.just("commandId"));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/docker/pull")
            .queryParam("image", "image")
            .build())
        .header("ApplicationId", "app")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo("commandId");
  }

  @Test
  public void shouldRunContainer() {
    given(service.run("name", "config"))
        .willReturn(Mono.just("started"));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/docker/run")
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
    given(service.start("containerId"))
        .willReturn(Mono.just(true));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/docker/start")
            .queryParam("containerId", "containerId")
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
    given(service.stop("containerId", 10))
        .willReturn(Mono.just(true));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/docker/stop")
            .queryParam("containerId", "containerId")
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
    given(service.logs("app", "containerId"))
        .willReturn(Mono.just("commandId"));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/docker/logs")
            .queryParam("containerId", "containerId")
            .build())
        .header("ApplicationId", "app")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo("commandId");
  }

  @Test
  public void shouldReturnTail() {
    given(service.tail("containerId", 100))
        .willReturn(Mono.just("log"));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/docker/tail")
            .queryParam("containerId", "containerId")
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo("log");
  }

  @Test
  public void shouldReturnTailCustomLines() {
    given(service.tail("containerId", 42))
        .willReturn(Mono.just("log"));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/docker/tail")
            .queryParam("containerId", "containerId")
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
    given(service.inspect("containerId"))
        .willReturn(Mono.just(DockerContainerTest.DOCKER_CONTAINER));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/docker/inspect")
            .queryParam("containerId", "containerId")
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
        .uri(uriBuilder -> uriBuilder.path("/docker/ps")
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
    given(service.rm("containerId"))
        .willReturn(Mono.just(true));

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/docker/rm")
            .queryParam("containerId", "containerId")
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody()
        .jsonPath("$")
        .isEqualTo("true");
  }

  @Test
  public void shouldImages() {
    given(service.images())
        .willReturn(Flux.just(DockerImageTest.DOCKER_IMAGE));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/docker/images")
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody()
        .jsonPath("$[0].name")
        .isEqualTo(DockerImageTest.DOCKER_IMAGE.getName());
  }

  @Test
  public void shouldRmi() {
    final var imageName = "image/test:version";
    given(service.rmi(imageName))
        .willReturn(Mono.just(true));

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/docker/rmi")
            .queryParam("imageId", imageName)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody()
        .jsonPath("$")
        .isEqualTo("true");
  }

  @Test
  public void shouldPrune() {
    given(service.prune("app", true, true))
        .willReturn(Mono.just("commandId"));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/docker/prune")
            .queryParam("all", "true")
            .queryParam("volumes", "true")
            .build())
        .header("ApplicationId", "app")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo("commandId");
  }

}
