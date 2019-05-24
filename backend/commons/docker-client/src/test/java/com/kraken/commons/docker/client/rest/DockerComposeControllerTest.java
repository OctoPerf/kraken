package com.kraken.commons.docker.client.rest;

import com.kraken.commons.docker.client.compose.DockerComposeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.kraken.test.utils.TestUtils.shouldPassNPE;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {DockerComposeController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class DockerComposeControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  DockerComposeService service;

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(DockerComposeController.class);
  }

  @Test
  public void shouldUp() {
    given(service.up("app", "path"))
        .willReturn(Mono.just("commandId"));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/docker-compose/up")
            .queryParam("path", "path")
            .build())
        .header("ApplicationId", "app")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo("commandId");
  }


  @Test
  public void shouldDown() {
    given(service.down("app", "path"))
        .willReturn(Mono.just("commandId"));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/docker-compose/down")
            .queryParam("path", "path")
            .build())
        .header("ApplicationId", "app")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo("commandId");
  }

  @Test
  public void shouldPs() {
    given(service.ps("app", "path"))
        .willReturn(Mono.just("commandId"));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/docker-compose/ps")
            .queryParam("path", "path")
            .build())
        .header("ApplicationId", "app")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo("commandId");
  }

  @Test
  public void shouldlogs() {
    given(service.logs("app", "path"))
        .willReturn(Mono.just("commandId"));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/docker-compose/logs")
            .queryParam("path", "path")
            .build())
        .header("ApplicationId", "app")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo("commandId");
  }

}
