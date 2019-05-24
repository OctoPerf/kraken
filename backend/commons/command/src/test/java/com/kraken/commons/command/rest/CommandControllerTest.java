package com.kraken.commons.command.rest;

import com.kraken.commons.command.entity.CommandLogTest;
import com.kraken.commons.command.executor.CommandExecutor;
import com.kraken.commons.command.entity.CommandTest;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.kraken.test.utils.TestUtils.shouldPassNPE;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {CommandController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class CommandControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  CommandExecutor executor;

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(CommandController.class);
  }

  @Test
  public void shouldExecute() {
    given(executor.execute(CommandTest.SHELL_COMMAND))
        .willReturn(Mono.just("commandId"));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/command/execute")
            .build())
        .body(BodyInserters.fromObject(CommandTest.SHELL_COMMAND))
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("commandId");
  }

  @Test
  public void shouldListen() {
    given(executor.listen("app"))
        .willReturn(Flux.just(CommandLogTest.SHELL_LOGS));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/command/listen/app")
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$[0].command.id")
        .isEqualTo(CommandLogTest.SHELL_LOGS.getCommand().getId());
  }

  @Test
  public void shouldCancel() {
    given(executor.cancel("commandId"))
        .willReturn(true);

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/command/cancel")
            .queryParam("commandId", "commandId")
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("true");
  }

}
