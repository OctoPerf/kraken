package com.kraken.commons.docker.spotify.rest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.commons.command.client.CommandClient;
import com.kraken.commons.command.entity.Command;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.kraken.test.utils.TestUtils.shouldPassNPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {DockerSystemController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class DockerSystemControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  CommandClient commandClient;

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(DockerSystemController.class);
  }


  @Test
  public void shouldPrune() {
    final var commandId = "commandId";
    final var applicationId = "app";

    given(commandClient.execute(any()))
        .willReturn(Mono.just(commandId));

    final Command command = Command.builder()
        .id("")
        .applicationId(applicationId)
        .command(ImmutableList.of("docker", "system", "prune", "-f"))
        .environment(ImmutableMap.of())
        .path("")
        .onCancel(ImmutableList.of())
        .build();

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/system/prune")
            .queryParam("all", "false")
            .queryParam("volumes", "false")
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
  public void shouldPruneAll() {
    final var commandId = "commandId";
    final var applicationId = "app";

    given(commandClient.execute(any()))
        .willReturn(Mono.just(commandId));

    final Command command = Command.builder()
        .id("")
        .applicationId(applicationId)
        .command(ImmutableList.of("docker", "system", "prune", "-f", "--all", "--volumes"))
        .environment(ImmutableMap.of())
        .path("")
        .onCancel(ImmutableList.of())
        .build();

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/system/prune")
            .queryParam("all", "true")
            .queryParam("volumes", "true")
            .build())
        .header("ApplicationId", applicationId)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo(commandId);

    verify(commandClient).execute(command);
  }
}
