package com.kraken.command.zt.rest;

import com.google.common.base.Charsets;
import com.kraken.command.entity.CommandLog;
import com.kraken.command.entity.CommandLogTest;
import com.kraken.command.entity.CommandTest;
import com.kraken.command.zt.executor.CommandExecutor;
import com.kraken.tools.sse.SSEService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

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

  @MockBean
  SSEService sse;

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(CommandController.class);
  }

  @Test
  public void shouldExecute() {
    given(executor.execute(CommandTest.SHELL_COMMAND))
        .willReturn(Mono.just("commandId"));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/execute")
            .build())
        .body(BodyInserters.fromObject(CommandTest.SHELL_COMMAND))
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("commandId");
  }

  @Test
  public void shouldListen() {
    final var flux = Flux.just(CommandLogTest.SHELL_LOGS, CommandLogTest.SHELL_LOGS);
    final var sseFlux = Flux.just(ServerSentEvent.builder(CommandLogTest.SHELL_LOGS).build(), ServerSentEvent.<CommandLog>builder().comment("keep alive").build(), ServerSentEvent.builder(CommandLogTest.SHELL_LOGS).build());

    given(executor.listen("app"))
        .willReturn(flux);

    given(sse.keepAlive(flux)).willReturn(sseFlux);

    final var result = webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/listen/app")
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
        .expectBody()
        .returnResult();

    final var body = new String(Optional.ofNullable(result.getResponseBody()).orElse(new byte[0]), Charsets.UTF_8);
    Assertions.assertThat(body).isEqualTo("data:{\"command\":{\"id\":\"id\",\"applicationId\":\"app\",\"command\":[\"java\",\"--version\"],\"environment\":{\"key\":\"value\"},\"path\":\".\",\"onCancel\":[]},\"status\":\"RUNNING\",\"text\":\"text\"}\n" +
        "\n" +
        ":keep alive\n" +
        "\n" +
        "data:{\"command\":{\"id\":\"id\",\"applicationId\":\"app\",\"command\":[\"java\",\"--version\"],\"environment\":{\"key\":\"value\"},\"path\":\".\",\"onCancel\":[]},\"status\":\"RUNNING\",\"text\":\"text\"}\n" +
        "\n");
  }

  @Test
  public void shouldCancel() {
    given(executor.cancel("commandId"))
        .willReturn(true);

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/cancel")
            .queryParam("commandId", "commandId")
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo("true");
  }

}
