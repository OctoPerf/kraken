package com.kraken.commons.analysis.server.rest;

import com.google.common.collect.ImmutableMap;
import com.kraken.commons.analysis.entity.Result;
import com.kraken.commons.analysis.entity.ResultStatus;
import com.kraken.commons.analysis.server.AnalysisPropertiesTest;
import com.kraken.commons.command.client.CommandClient;
import com.kraken.commons.command.entity.Command;
import com.kraken.commons.grafana.client.GrafanaClient;
import com.kraken.commons.influxdb.client.InfluxDBClient;
import com.kraken.commons.storage.client.StorageClient;
import com.kraken.commons.storage.entity.StorageNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;

import static com.kraken.commons.analysis.entity.ResultType.DEBUG;
import static com.kraken.commons.analysis.entity.ResultType.RUN;
import static com.kraken.commons.gatling.properties.GatlingPropertiesTest.GATLING_PROPERTIES;
import static com.kraken.commons.grafana.client.GrafanaClientPropertiesTest.GRAFANA_CLIENT_PROPERTIES;
import static com.kraken.commons.influxdb.client.InfluxDBClientPropertiesTest.INFLUX_DB_CLIENT_PROPERTIES;
import static com.kraken.commons.storage.entity.StorageNodeType.DIRECTORY;
import static com.kraken.commons.storage.entity.StorageNodeType.FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AnalysisServiceTest {

  @Mock
  private StorageClient storageClient;
  @Mock
  private CommandClient executorClient;
  @Mock
  private GrafanaClient grafanaClient;
  @Mock
  private InfluxDBClient influxdbClient;

  private AnalysisService service;

  @Before
  public void before() {
    service = new AnalysisService(AnalysisPropertiesTest.ANALYSIS_PROPERTIES,
        INFLUX_DB_CLIENT_PROPERTIES,
        GRAFANA_CLIENT_PROPERTIES,
        GATLING_PROPERTIES,
        storageClient,
        executorClient,
        grafanaClient,
        influxdbClient);
  }

  @Test
  public void shouldRun() {
    final var dashboard = "dashboard";
    final var commandId = "commandId";
    final var applicationId = "applicationId";
    final var runDescription = "runDescription";
    final Map<String, String> environment = ImmutableMap.of("KEY", "value");
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
    given(executorClient.execute(any(Command.class))).willReturn(Mono.just(commandId));

    given(storageClient.getContent(GRAFANA_CLIENT_PROPERTIES.getGrafanaDashboard())).willReturn(Mono.just(dashboard));
    given(grafanaClient.initDashboard(anyString(), anyString(), any(Long.class), anyString())).willReturn(dashboard);
    given(grafanaClient.importDashboard(anyString())).willReturn(Mono.just("dashboard set"));

    final var response = service.run(applicationId, runDescription, environment).block();
    assertThat(response).isEqualTo(commandId);

    final ArgumentCaptor<Result> resultCaptor = ArgumentCaptor.forClass(Result.class);
    verify(storageClient).setJsonContent(anyString(), resultCaptor.capture());
    final Result result = resultCaptor.getValue();
    assertThat(result.getStatus()).isEqualTo(ResultStatus.STARTING);
    assertThat(result.getType()).isEqualTo(RUN);
    assertThat(result.getRunDescription()).isEqualTo(runDescription);

    final ArgumentCaptor<Command> commandCaptor = ArgumentCaptor.forClass(Command.class);
    verify(executorClient).execute(commandCaptor.capture());
    final Command command = commandCaptor.getValue();
    assertThat(command.getApplicationId()).isEqualTo(applicationId);
    assertThat(command.getPath()).isEqualTo(AnalysisPropertiesTest.ANALYSIS_PROPERTIES.getRunProperties().getRoot());
    assertThat(command.getCommand()).isEqualTo(Arrays.asList("/bin/sh", "-c", "chmod +x runnerScript && ./runnerScript"));
    assertThat(command.getOnCancel()).isEqualTo(Arrays.asList("/bin/sh", "-c", "chmod +x runnerCancelScript && ./runnerCancelScript"));
    assertThat(command.getEnvironment().keySet().size()).isEqualTo(8);

    verify(grafanaClient).importDashboard(dashboard);
  }

  @Test
  public void shouldDebug() {
    final var commandId = "commandId";
    final var applicationId = "applicationId";
    final var runDescription = "runDescription";
    final Map<String, String> environment = ImmutableMap.of("KEY", "value");
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
    given(executorClient.execute(any(Command.class))).willReturn(Mono.just(commandId));

    final var response = service.debug(applicationId, runDescription, environment).block();
    assertThat(response).isEqualTo(commandId);

    final ArgumentCaptor<Result> resultCaptor = ArgumentCaptor.forClass(Result.class);
    verify(storageClient).setJsonContent(anyString(), resultCaptor.capture());
    final Result result = resultCaptor.getValue();
    assertThat(result.getStatus()).isEqualTo(ResultStatus.STARTING);
    assertThat(result.getType()).isEqualTo(DEBUG);
    assertThat(result.getRunDescription()).isEqualTo(runDescription);

    final ArgumentCaptor<Command> commandCaptor = ArgumentCaptor.forClass(Command.class);
    verify(executorClient).execute(commandCaptor.capture());
    final Command command = commandCaptor.getValue();
    assertThat(command.getApplicationId()).isEqualTo(applicationId);
    assertThat(command.getPath()).isEqualTo(AnalysisPropertiesTest.ANALYSIS_PROPERTIES.getDebugProperties().getRoot());
    assertThat(command.getCommand()).isEqualTo(Arrays.asList("/bin/sh", "-c", "chmod +x debuggerScript && ./debuggerScript"));
    assertThat(command.getOnCancel()).isEqualTo(Arrays.asList("/bin/sh", "-c", "chmod +x debuggerCancelScript && ./debuggerCancelScript"));
    assertThat(command.getEnvironment().keySet().size()).isEqualTo(8);
  }

  @Test
  public void shouldRecord() {
    final var commandId = "commandId";
    final var applicationId = "applicationId";
    final Map<String, String> environment = ImmutableMap.of("KEY", "value");

    given(executorClient.execute(any(Command.class))).willReturn(Mono.just(commandId));

    final var response = service.record(applicationId, environment).block();
    assertThat(response).isEqualTo(commandId);

    final ArgumentCaptor<Command> commandCaptor = ArgumentCaptor.forClass(Command.class);
    verify(executorClient).execute(commandCaptor.capture());
    final Command command = commandCaptor.getValue();
    assertThat(command.getApplicationId()).isEqualTo(applicationId);
    assertThat(command.getPath()).isEqualTo(AnalysisPropertiesTest.ANALYSIS_PROPERTIES.getRecordProperties().getRoot());
    assertThat(command.getCommand()).isEqualTo(Arrays.asList("/bin/sh", "-c", "chmod +x recorderScript && ./recorderScript"));
    assertThat(command.getEnvironment().keySet().size()).isEqualTo(3);
  }

  @Test
  public void shouldDeleteRun() {
    final var testId = "testId";
    final var result = Result.builder().id("id").startDate(42L).endDate(42L).runDescription("runDescription").status(ResultStatus.STARTING).type(RUN).build();
    given(storageClient.getJsonContent("resultsRoot/testId/result.json", Result.class)).willReturn(Mono.just(result));
    given(grafanaClient.deleteDashboard(testId)).willReturn(Mono.just("dashboard deleted"));
    given(storageClient.delete("resultsRoot/testId")).willReturn(Mono.just(true));
    given(influxdbClient.deleteSeries(testId)).willReturn(Mono.just("ok"));
    final var response = service.delete(testId).block();
    assertThat(response).isEqualTo(testId);
    verify(storageClient).delete("resultsRoot/testId");
    verify(grafanaClient).deleteDashboard(testId);
    verify(influxdbClient).deleteSeries(testId);
  }

  @Test
  public void shouldDeleteDebug() {
    final var testId = "testId";
    final var result = Result.builder().id("id").startDate(42L).endDate(42L).runDescription("runDescription").status(ResultStatus.STARTING).type(DEBUG).build();
    given(storageClient.getJsonContent("resultsRoot/testId/result.json", Result.class)).willReturn(Mono.just(result));
    given(storageClient.delete("resultsRoot/testId")).willReturn(Mono.just(true));
    final var response = service.delete(testId).block();
    assertThat(response).isEqualTo(testId);
    verify(storageClient).delete("resultsRoot/testId");
    verify(grafanaClient, never()).deleteDashboard(anyString());
    verify(influxdbClient, never()).deleteSeries(anyString());

  }

  @Test
  public void shouldSetStatus() {
    final var testId = "testId";
    final var dashboard = "dashboard";
    final var result = Result.builder().id("id").startDate(42L).endDate(42L).runDescription("runDescription").status(ResultStatus.STARTING).type(RUN).build();
    final var resultNode = StorageNode.builder()
        .depth(1)
        .path("path/result.son")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();

    given(storageClient.getJsonContent("resultsRoot/testId/result.json", Result.class)).willReturn(Mono.just(result));
    given(storageClient.setJsonContent("resultsRoot/testId/result.json", result.withStatus(ResultStatus.RUNNING).withEndDate(0L))).willReturn(Mono.just(resultNode));
    given(grafanaClient.getDashboard(testId)).willReturn(Mono.just(dashboard));
    given(grafanaClient.updatedDashboard(0L, dashboard)).willReturn(dashboard);
    given(grafanaClient.setDashboard(dashboard)).willReturn(Mono.just("dashboard set"));

    final var response = service.setStatus(testId, ResultStatus.RUNNING).block();
    assertThat(response).isEqualTo(resultNode);

    verify(grafanaClient).setDashboard(dashboard);
    verify(storageClient).setJsonContent(anyString(), any(Result.class));
  }

  @Test
  public void shouldSetCompletedStatus() {
    final var testId = "testId";
    final var dashboard = "dashboard";
    final var result = Result.builder().id("id").startDate(42L).endDate(42L).runDescription("runDescription").status(ResultStatus.STARTING).type(RUN).build();
    final var resultNode = StorageNode.builder()
        .depth(1)
        .path("path/result.son")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();

    given(storageClient.getJsonContent("resultsRoot/testId/result.json", Result.class)).willReturn(Mono.just(result));
    given(storageClient.setJsonContent(anyString(), any(Result.class))).willReturn(Mono.just(resultNode));
    given(grafanaClient.getDashboard(testId)).willReturn(Mono.just(dashboard));
    given(grafanaClient.updatedDashboard(any(Long.class), anyString())).willReturn(dashboard);
    given(grafanaClient.setDashboard(dashboard)).willReturn(Mono.just("dashboard set"));

    final var response = service.setStatus(testId, ResultStatus.COMPLETED).block();
    assertThat(response).isEqualTo(resultNode);

    final ArgumentCaptor<Result> resultCaptor = ArgumentCaptor.forClass(Result.class);
    verify(storageClient).setJsonContent(anyString(), resultCaptor.capture());
    final Result _result = resultCaptor.getValue();
    assertThat(_result.getStatus()).isEqualTo(ResultStatus.COMPLETED);
    assertThat(_result.getEndDate()).isNotEqualTo(result.getEndDate());

    verify(grafanaClient).setDashboard(dashboard);
  }

  @Test
  public void shouldDebugSetStatus() {
    final var testId = "testId";
    final var result = Result.builder().id("id").startDate(42L).endDate(42L).runDescription("runDescription").status(ResultStatus.STARTING).type(DEBUG).build();
    final var resultNode = StorageNode.builder()
        .depth(1)
        .path("path/result.son")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();

    given(storageClient.getJsonContent("resultsRoot/testId/result.json", Result.class)).willReturn(Mono.just(result));
    given(storageClient.setJsonContent("resultsRoot/testId/result.json", result.withStatus(ResultStatus.RUNNING).withEndDate(0L))).willReturn(Mono.just(resultNode));

    final var response = service.setStatus(testId, ResultStatus.RUNNING).block();
    assertThat(response).isEqualTo(resultNode);

    verify(grafanaClient, never()).getDashboard(anyString());
    verify(storageClient).setJsonContent(anyString(), any(Result.class));
  }

}
