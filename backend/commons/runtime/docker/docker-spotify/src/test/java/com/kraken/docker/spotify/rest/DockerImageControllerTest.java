package com.kraken.docker.spotify.rest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.command.client.CommandClient;
import com.kraken.command.entity.Command;
import com.kraken.docker.entity.DockerImageTest;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.kraken.test.utils.TestUtils.shouldPassNPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {DockerImageController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class DockerImageControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  DockerService service;

  @MockBean
  CommandClient commandClient;

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(DockerImageController.class);
  }

  @Test
  public void shouldPullImage() {
    final var commandId = "commandId";

    given(commandClient.execute(any()))
        .willReturn(Mono.just(commandId));

    final var image = "octoperf/image:latest";
    final var application = "app";

    final Command command = Command.builder()
        .id("")
        .applicationId("app")
        .command(ImmutableList.of("docker", "pull", image))
        .environment(ImmutableMap.of())
        .path("")
        .onCancel(ImmutableList.of())
        .build();

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/image/pull")
            .queryParam("image", image)
            .build())
        .header("ApplicationId", application)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo(commandId);

    verify(commandClient).execute(command);
  }

  @Test
  public void shouldImages() {
    given(service.images())
        .willReturn(Flux.just(DockerImageTest.DOCKER_IMAGE));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/image")
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
        .uri(uriBuilder -> uriBuilder.path("/image")
            .queryParam("imageId", imageName)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody()
        .jsonPath("$")
        .isEqualTo("true");
  }
}
