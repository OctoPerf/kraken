package com.kraken.analysis.server.rest;

import com.kraken.analysis.entity.DebugEntryTest;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultTest;
import com.kraken.analysis.server.rest.AnalysisController;
import com.kraken.analysis.server.service.AnalysisService;
import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageNodeTest;
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
  public void shouldCreate() {
    final var result = ResultTest.RESULT;
    final var node = StorageNodeTest.STORAGE_NODE;
    given(service.create(result))
        .willReturn(Mono.just(node));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/result")
            .build())
        .body(BodyInserters.fromValue(result))
        .exchange()
        .expectStatus().isOk()
        .expectBody(StorageNode.class)
        .isEqualTo(node);
  }

  @Test
  public void shouldDelete() {
    final var resultId = "resultId";
    given(service.delete(resultId))
        .willReturn(Mono.just(resultId));

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/result")
            .queryParam("resultId", resultId)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo(resultId);
  }

  @Test
  public void shouldSetStatus() {
    final var resultId = "resultId";
    final var status = ResultStatus.COMPLETED;
    final var resultNode = StorageNode.builder()
        .depth(1)
        .path("path/result.son")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();

    given(service.setStatus(resultId, status))
        .willReturn(Mono.just(resultNode));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/result/status/COMPLETED")
            .queryParam("resultId", resultId)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody(StorageNode.class)
        .isEqualTo(resultNode);
  }

  @Test
  public void shouldAddDebug() {
    final var debug = DebugEntryTest.DEBUG_ENTRY;

    given(service.addDebug(debug))
        .willReturn(Mono.fromCallable(() -> null));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/result/debug")
            .build())
        .body(BodyInserters.fromValue(debug))
        .exchange()
        .expectStatus().isOk();
  }

}

