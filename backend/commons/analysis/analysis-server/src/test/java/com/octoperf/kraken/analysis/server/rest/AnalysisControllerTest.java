package com.octoperf.kraken.analysis.server.rest;

import com.octoperf.kraken.analysis.entity.DebugEntryTest;
import com.octoperf.kraken.analysis.entity.GrafanaLogin;
import com.octoperf.kraken.analysis.entity.ResultStatus;
import com.octoperf.kraken.analysis.entity.ResultTest;
import com.octoperf.kraken.analysis.service.api.AnalysisService;
import com.octoperf.kraken.config.grafana.api.GrafanaProperties;
import com.octoperf.kraken.storage.entity.StorageNode;
import com.octoperf.kraken.storage.entity.StorageNodeTest;
import com.octoperf.kraken.tests.utils.TestUtils;
import com.octoperf.kraken.tests.web.security.AuthControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseCookie;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static com.octoperf.kraken.storage.entity.StorageNodeType.FILE;
import static org.mockito.BDDMockito.given;

public class AnalysisControllerTest extends AuthControllerTest {

  @MockBean
  AnalysisService analysisService;
  @MockBean
  GrafanaProperties grafanaProperties;

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(AnalysisController.class);
  }

  @Test
  public void shouldCreate() {
    final var result = ResultTest.RESULT;
    final var node = StorageNodeTest.STORAGE_NODE;

    given(analysisService.createResult(userOwner(), result))
        .willReturn(Mono.just(node));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/result")
            .build())
        .header("Authorization", "Bearer user-token")
        .body(BodyInserters.fromValue(result))
        .exchange()
        .expectStatus().isOk()
        .expectBody(StorageNode.class)
        .isEqualTo(node);
  }

  @Test
  public void shouldCreateForbidden() {
    final var result = ResultTest.RESULT;
    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/result")
            .build())
        .header("Authorization", "Bearer no-role-token")
        .body(BodyInserters.fromValue(result))
        .exchange()
        .expectStatus().is4xxClientError();
  }

  @Test
  public void shouldDelete() {
    final var resultId = "resultId";
    given(analysisService.deleteResult(userOwner(), resultId))
        .willReturn(Mono.just(resultId));

    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/result")
            .queryParam("resultId", resultId)
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .isEqualTo(resultId);
  }

  @Test
  public void shouldDeleteForbidden() {
    final var resultId = "resultId";
    webTestClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/result")
            .queryParam("resultId", resultId)
            .build())
        .header("Authorization", "Bearer no-role-token")
        .exchange()
        .expectStatus().is4xxClientError();
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

    given(analysisService.setResultStatus(userOwner(), resultId, status))
        .willReturn(Mono.just(resultNode));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/result/status/COMPLETED")
            .queryParam("resultId", resultId)
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectBody(StorageNode.class)
        .isEqualTo(resultNode);
  }

  @Test
  public void shouldSetStatusForbidden() {
    final var resultId = "resultId";
    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/result/status/COMPLETED")
            .queryParam("resultId", resultId)
            .build())
        .header("Authorization", "Bearer no-role-token")
        .exchange()
        .expectStatus().is4xxClientError();
  }

  @Test
  public void shouldAddDebug() {
    final var debug = DebugEntryTest.DEBUG_ENTRY;

    given(analysisService.addDebugEntry(userOwner(), debug))
        .willReturn(Mono.fromCallable(() -> null));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/result/debug")
            .build())
        .header("Authorization", "Bearer user-token")
        .body(BodyInserters.fromValue(debug))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void shouldAddDebugForbidden() {
    final var debug = DebugEntryTest.DEBUG_ENTRY;
    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/result/debug")
            .build())
        .header("Authorization", "Bearer no-role-token")
        .body(BodyInserters.fromValue(debug))
        .exchange()
        .expectStatus().is4xxClientError();
  }

  @Test
  public void shouldGrafanaLogin() {
    final var grafanaUrl = "url";
    given(grafanaProperties.getPublishedUrl()).willReturn(grafanaUrl);
    given(analysisService.grafanaLogin(userOwner())).willReturn(Mono.just(ResponseCookie.from("grafana_session", "sessionId").build()));
    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/result/grafana/login")
            .queryParam("resultId", "test")
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody(GrafanaLogin.class).isEqualTo(GrafanaLogin.builder().session("sessionId").url("url/d/test").build());
  }
}

