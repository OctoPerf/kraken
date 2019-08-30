package com.kraken.tools.sse;

import com.google.common.base.Charsets;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SpringSSEService.class, SSETestController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class SpringSSEServiceIntegrationTest {

  @Autowired
  WebTestClient webTestClient;

  @Test
  public void shouldKeepAlive() {
    final var result = webTestClient.get()
        .uri("/test/watch")
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
        .expectBody()
        .returnResult();
    final var body = new String(Optional.ofNullable(result.getResponseBody()).orElse(new byte[0]), Charsets.UTF_8);
    Assertions.assertThat(body).isEqualTo("data:event0\n" +
        "\n" +
        ":keep alive\n" +
        "\n" +
        "data:event1\n" +
        "\n" +
        ":keep alive\n" +
        "\n" +
        "data:event2\n" +
        "\n" +
        "data:event3\n"+
        "\n");
  }

  @Test
  public void shouldMerge() {
    final var result = webTestClient.get()
        .uri("/test/merge")
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
        .expectBody()
        .returnResult();
    final var body = new String(Optional.ofNullable(result.getResponseBody()).orElse(new byte[0]), Charsets.UTF_8);
    Assertions.assertThat(body).isEqualTo("data:{\"type\":\"String\",\"value\":\"event0\"}\n" +
        "\n" +
        "data:{\"type\":\"Long\",\"value\":0}\n" +
        "\n" +
        "data:{\"type\":\"String\",\"value\":\"event1\"}\n" +
        "\n" +
        "data:{\"type\":\"Long\",\"value\":1}\n" +
        "\n" +
        "data:{\"type\":\"String\",\"value\":\"event2\"}\n" +
        "\n" +
        "data:{\"type\":\"Long\",\"value\":2}\n" +
        "\n");
  }
}
