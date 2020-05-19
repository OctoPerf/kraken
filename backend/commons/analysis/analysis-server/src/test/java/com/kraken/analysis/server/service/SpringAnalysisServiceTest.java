package com.kraken.analysis.server.service;

import com.kraken.analysis.entity.*;
import com.kraken.config.grafana.api.AnalysisResultsProperties;
import com.kraken.config.grafana.api.GrafanaProperties;
import com.kraken.grafana.client.api.GrafanaUserClient;
import com.kraken.grafana.client.api.GrafanaUserClientBuilder;
import com.kraken.grafana.client.api.GrafanaUserConverter;
import com.kraken.grafana.client.api.GrafanaUserTest;
import com.kraken.influxdb.client.api.InfluxDBClient;
import com.kraken.influxdb.client.api.InfluxDBUserConverter;
import com.kraken.influxdb.client.api.InfluxDBUserTest;
import com.kraken.security.admin.client.api.SecurityAdminClient;
import com.kraken.security.entity.functions.api.OwnerToApplicationId;
import com.kraken.security.entity.functions.api.OwnerToUserId;
import com.kraken.security.entity.owner.PublicOwner;
import com.kraken.security.entity.user.KrakenUserTest;
import com.kraken.storage.client.api.StorageClient;
import com.kraken.storage.client.api.StorageClientBuilder;
import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageNodeTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.file.Paths;
import java.util.Optional;

import static com.kraken.storage.entity.StorageNodeType.DIRECTORY;
import static com.kraken.storage.entity.StorageNodeType.FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpringAnalysisServiceTest {

  @Mock(lenient = true)
  StorageClientBuilder storageClientBuilder;
  @Mock(lenient = true)
  StorageClient storageClient;
  @Mock(lenient = true)
  GrafanaUserClientBuilder grafanaUserClientBuilder;
  @Mock
  GrafanaUserClient grafanaUserClient;
  @Mock(lenient = true)
  GrafanaUserConverter grafanaUserConverter;
  @Mock
  InfluxDBClient influxdbClient;
  @Mock(lenient = true)
  InfluxDBUserConverter influxDBUserConverter;
  @Mock(lenient = true)
  HeadersToExtension headersToExtension;
  @Mock(lenient = true)
  StatusToEndDate statusToEndDate;
  @Mock(lenient = true)
  AnalysisResultsProperties analysisResultsProperties;
  @Mock(lenient = true)
  GrafanaProperties grafanaProperties;
  @Mock(lenient = true)
  OwnerToUserId toUserId;
  @Mock(lenient = true)
  OwnerToApplicationId toApplicationId;
  @Mock(lenient = true)
  SecurityAdminClient securityAdminClient;

  private AnalysisService service;

  @BeforeEach
  public void before() {
    given(headersToExtension.apply(anyList())).willReturn(".txt");
    given(statusToEndDate.apply(any(ResultStatus.class))).willReturn(0L);
    given(grafanaProperties.getDashboard()).willReturn("dashboard");
    given(analysisResultsProperties.getResultPath(anyString())).willAnswer(args -> Paths.get("resultsRoot", args.getArgument(0, String.class)));
    given(storageClientBuilder.mode(any())).willReturn(storageClientBuilder);
    given(storageClientBuilder.mode(any(), any())).willReturn(storageClientBuilder);
    given(storageClientBuilder.applicationId(any())).willReturn(storageClientBuilder);
    given(storageClientBuilder.build()).willReturn(storageClient);
    given(toApplicationId.apply(any())).willReturn(Optional.of("app"));
    given(toUserId.apply(any())).willReturn(Optional.of("user-id"));
    given(securityAdminClient.getUser(any())).willReturn(Mono.just(KrakenUserTest.KRAKEN_USER));
    given(influxDBUserConverter.apply(any())).willReturn(InfluxDBUserTest.INFLUX_DB_USER);
    given(grafanaUserConverter.apply(any())).willReturn(GrafanaUserTest.GRAFANA_USER);
    given(grafanaUserClientBuilder.grafanaUser(any())).willReturn(grafanaUserClientBuilder);
    given(grafanaUserClientBuilder.influxDBUser(any())).willReturn(grafanaUserClientBuilder);
    given(grafanaUserClientBuilder.build()).willReturn(Mono.just(grafanaUserClient));

    service = new SpringAnalysisService(
        analysisResultsProperties,
        grafanaProperties,
        influxdbClient,
        influxDBUserConverter,
        grafanaUserClientBuilder,
        grafanaUserConverter,
        storageClientBuilder,
        securityAdminClient,
        toApplicationId,
        toUserId,
        headersToExtension,
        statusToEndDate
    );
  }

  @Test
  public void shouldDeleteRun() {
    final var resultId = "resultId";
    final var result = ResultTest.RESULT;
    given(storageClient.getJsonContent("resultsRoot/resultId/result.json", Result.class)).willReturn(Mono.just(result));
    given(grafanaUserClient.deleteDashboard(resultId)).willReturn(Mono.just("dashboard deleted"));
    given(storageClient.delete("resultsRoot/resultId")).willReturn(Mono.just(true));
    given(influxdbClient.deleteSeries(InfluxDBUserTest.INFLUX_DB_USER.getDatabase(), resultId)).willReturn(Mono.just("ok"));
    final var response = service.delete(PublicOwner.INSTANCE, resultId).block();
    assertThat(response).isEqualTo(resultId);
    verify(storageClient).delete("resultsRoot/resultId");
    verify(grafanaUserClient).deleteDashboard(resultId);
    verify(influxdbClient).deleteSeries(InfluxDBUserTest.INFLUX_DB_USER.getDatabase(), resultId);
  }

  @Test
  public void shouldDeleteDebug() {
    final var resultId = "resultId";
    final var result = ResultTest.DEBUG_RESULT;
    given(storageClient.getJsonContent("resultsRoot/resultId/result.json", Result.class)).willReturn(Mono.just(result));
    given(storageClient.delete("resultsRoot/resultId")).willReturn(Mono.just(true));
    final var response = service.delete(PublicOwner.INSTANCE, resultId).block();
    assertThat(response).isEqualTo(resultId);
    verify(storageClient).delete("resultsRoot/resultId");
    verify(grafanaUserClient, never()).deleteDashboard(anyString());
    verify(influxdbClient, never()).deleteSeries(anyString(), anyString());

  }

  @Test
  public void shouldSetStatus() {
    final var resultId = "resultId";
    final var dashboard = "dashboard";
    final var result = ResultTest.RESULT;
    final var resultNode = StorageNode.builder()
        .depth(1)
        .path("path/result.son")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();

    given(storageClient.getJsonContent("resultsRoot/resultId/result.json", Result.class)).willReturn(Mono.just(result));
    given(storageClient.setJsonContent("resultsRoot/resultId/result.json", result.withStatus(ResultStatus.RUNNING).withEndDate(0L))).willReturn(Mono.just(resultNode));
    given(grafanaUserClient.updateDashboard(resultId, 0L)).willReturn(Mono.just(dashboard));

    final var response = service.setStatus(PublicOwner.INSTANCE, resultId, ResultStatus.RUNNING).block();
    assertThat(response).isEqualTo(resultNode);

    verify(grafanaUserClient).updateDashboard(resultId, 0L);
    verify(storageClient).setJsonContent(anyString(), any(Result.class));
  }

  @Test
  public void shouldSetCompletedStatus() {
    final var resultId = "resultId";
    final var dashboard = "dashboard";
    final var result = ResultTest.RESULT;
    final var resultNode = StorageNode.builder()
        .depth(1)
        .path("path/result.son")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();

    given(storageClient.getJsonContent("resultsRoot/resultId/result.json", Result.class)).willReturn(Mono.just(result));
    given(storageClient.setJsonContent(anyString(), any(Result.class))).willReturn(Mono.just(resultNode));
    given(grafanaUserClient.updateDashboard(anyString(), any(Long.class))).willReturn(Mono.just(dashboard));

    final var response = service.setStatus(PublicOwner.INSTANCE, resultId, ResultStatus.COMPLETED).block();
    assertThat(response).isEqualTo(resultNode);

    final ArgumentCaptor<Result> resultCaptor = ArgumentCaptor.forClass(Result.class);
    verify(storageClient).setJsonContent(anyString(), resultCaptor.capture());
    final Result _result = resultCaptor.getValue();
    assertThat(_result.getStatus()).isEqualTo(ResultStatus.COMPLETED);
    assertThat(_result.getEndDate()).isNotEqualTo(result.getEndDate());

    verify(grafanaUserClient).updateDashboard(anyString(), any(Long.class));
  }

  @Test
  public void shouldDebugSetStatus() {
    final var resultId = "resultId";
    final var result = ResultTest.DEBUG_RESULT;
    final var resultNode = StorageNode.builder()
        .depth(1)
        .path("path/result.son")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();

    given(storageClient.getJsonContent("resultsRoot/resultId/result.json", Result.class)).willReturn(Mono.just(result));
    given(storageClient.setJsonContent("resultsRoot/resultId/result.json", result.withStatus(ResultStatus.RUNNING).withEndDate(0L))).willReturn(Mono.just(resultNode));

    final var response = service.setStatus(PublicOwner.INSTANCE, resultId, ResultStatus.RUNNING).block();
    assertThat(response).isEqualTo(resultNode);

    verify(grafanaUserClient, never()).updateDashboard(anyString(), anyLong());
    verify(storageClient).setJsonContent(anyString(), any(Result.class));
  }

  @Test
  public void shouldSetStatusFail() {
    final var resultId = "resultId";
    final var result = Result.builder()
        .id(resultId)
        .startDate(42L)
        .endDate(1337L)
        .status(ResultStatus.COMPLETED)
        .description("description")
        .type(ResultType.RUN)
        .build();

    given(storageClient.getJsonContent("resultsRoot/resultId/result.json", Result.class)).willReturn(Mono.just(result));

    // No error thrown if Result status is already terminal
    StepVerifier.create(service.setStatus(PublicOwner.INSTANCE, resultId, ResultStatus.RUNNING))
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldAddDebug() {
    final var debug = DebugEntryTest.DEBUG_ENTRY;
    final var debugEntry = debug.withRequestBodyFile("id-request.txt").withResponseBodyFile("id-response.txt");
    given(storageClient.setContent("resultsRoot/resultId/debug/id-request.txt", debug.getRequestBodyFile())).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));
    given(storageClient.setContent("resultsRoot/resultId/debug/id-response.txt", debug.getResponseBodyFile())).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));
    given(storageClient.setJsonContent("resultsRoot/resultId/debug/id.debug", debugEntry)).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));

    final var response = service.addDebug(PublicOwner.INSTANCE, debug).block();
    assertThat(response).isEqualTo(debugEntry);

    verify(storageClient).setContent("resultsRoot/resultId/debug/id-request.txt", debug.getRequestBodyFile());
    verify(storageClient).setContent("resultsRoot/resultId/debug/id-response.txt", debug.getResponseBodyFile());
    verify(storageClient).setJsonContent("resultsRoot/resultId/debug/id.debug", debugEntry);
  }

  @Test
  public void shouldAddDebugNoFiles() {
    final var debug = DebugEntryTest.DEBUG_ENTRY.withRequestBodyFile("").withResponseBodyFile("");
    given(storageClient.setJsonContent("resultsRoot/resultId/debug/id.debug", debug)).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));

    final var response = service.addDebug(PublicOwner.INSTANCE, debug).block();
    assertThat(response).isEqualTo(debug);

    verify(storageClient, never()).setContent(anyString(), anyString());
    verify(storageClient).setJsonContent("resultsRoot/resultId/debug/id.debug", debug);
  }

  @Test
  public void shouldCreateTestResult() {
    final var result = ResultTest.RESULT;
    final var dashboard = "dashboard";
    final var directoryNode = StorageNode.builder()
        .depth(0)
        .path("path")
        .type(DIRECTORY)
        .length(0L)
        .lastModified(0L)
        .build();
    final var resultNode = StorageNode.builder()
        .depth(1)
        .path("path/result.son")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();

    given(storageClient.createFolder(anyString())).willReturn(Mono.just(directoryNode));
    given(storageClient.setJsonContent(anyString(), any(Result.class))).willReturn(Mono.just(resultNode));

    given(storageClient.getContent(grafanaProperties.getDashboard())).willReturn(Mono.just(dashboard));
    given(grafanaUserClient.importDashboard(anyString(), anyString(), anyLong(), anyString())).willReturn(Mono.just("dashboard set"));

    final var response = service.create(PublicOwner.INSTANCE, result).block();
    assertThat(response).isEqualTo(resultNode);

    verify(storageClient).setJsonContent("resultsRoot/id/result.json", result);
    verify(grafanaUserClient).importDashboard(anyString(), anyString(), anyLong(), anyString());
  }

  @Test
  public void shouldCreateDebugResult() {
    final var result = ResultTest.DEBUG_RESULT;
    final var directoryNode = StorageNode.builder()
        .depth(0)
        .path("path")
        .type(DIRECTORY)
        .length(0L)
        .lastModified(0L)
        .build();
    final var resultNode = StorageNode.builder()
        .depth(1)
        .path("path/result.son")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();

    given(storageClient.createFolder(anyString())).willReturn(Mono.just(directoryNode));
    given(storageClient.setJsonContent(anyString(), any(Result.class))).willReturn(Mono.just(resultNode));

    final var response = service.create(PublicOwner.INSTANCE, result).block();
    assertThat(response).isEqualTo(resultNode);

    verify(storageClient).setJsonContent("resultsRoot/id/result.json", result);
    verify(grafanaUserClient, never()).importDashboard(anyString(), anyString(), anyLong(), anyString());
  }

  @Test
  public void shouldGrafanaLogin() {
    final var cookie = ResponseCookie.from("grafana_session", "sessionId").build();
    given(grafanaUserClientBuilder.getSessionCookie()).willReturn(Mono.just(cookie));
    final var response = service.grafanaLogin(PublicOwner.INSTANCE).block();
    assertThat(response).isEqualTo(cookie);
  }

}
