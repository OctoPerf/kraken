package com.kraken.analysis.server;

import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.storage.entity.StorageNode;
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

import java.util.Map;

import static com.kraken.storage.entity.StorageNodeType.FILE;
import static com.kraken.test.utils.TestUtils.shouldPassNPE;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {AnalysisController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class AnalysisControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  AnalysisService service;

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(AnalysisController.class);
  }

  @Test
  public void shouldDelete() {
    final var testId = "testId";
    given(service.delete(testId))
        .willReturn(Mono.just(testId));

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/test/delete")
            .queryParam("testId", testId)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo(testId);
  }

  @Test
  public void shouldSetStatus() {
    final var testId = "testId";
    final var status = ResultStatus.COMPLETED;
    final var resultNode = StorageNode.builder()
        .depth(1)
        .path("path/result.son")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();

    given(service.setStatus(testId, status))
        .willReturn(Mono.just(resultNode));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/test/status/COMPLETED")
            .queryParam("testId", testId)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody(StorageNode.class)
        .isEqualTo(resultNode);
  }

  @Test
  public void shouldRun() {
    final var applicationId = "applicationId";
    final var runDescription = "runDescription";
    final Map<String, String> environment = ImmutableMap.of();
    final var commandId = "commandId";

    given(service.run(applicationId, runDescription, environment))
        .willReturn(Mono.just(commandId));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/test/run")
            .queryParam("runDescription", "runDescription")
            .build())
        .header("ApplicationId", "applicationId")
        .body(BodyInserters.fromObject(environment))
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo(commandId);
  }

  @Test
  public void shouldDebug() {
    final var applicationId = "applicationId";
    final var runDescription = "runDescription";
    final Map<String, String> environment = ImmutableMap.of();
    final var commandId = "commandId";

    given(service.debug(applicationId, runDescription, environment))
        .willReturn(Mono.just(commandId));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/test/debug")
            .queryParam("runDescription", "runDescription")
            .build())
        .header("ApplicationId", "applicationId")
        .body(BodyInserters.fromObject(environment))
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo(commandId);
  }

  @Test
  public void shouldRecord() {
    final var applicationId = "applicationId";
    final Map<String, String> environment = ImmutableMap.of();
    final var commandId = "commandId";

    given(service.record(applicationId, environment))
        .willReturn(Mono.just(commandId));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/test/record")
            .build())
        .header("ApplicationId", "applicationId")
        .body(BodyInserters.fromObject(environment))
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo(commandId);
  }
}
