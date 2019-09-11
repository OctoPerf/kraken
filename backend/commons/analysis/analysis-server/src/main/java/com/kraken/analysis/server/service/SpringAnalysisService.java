package com.kraken.analysis.server.service;

import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.analysis.entity.HttpHeader;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.server.properties.AnalysisProperties;
import com.kraken.grafana.client.GrafanaClient;
import com.kraken.grafana.client.GrafanaClientProperties;
import com.kraken.influxdb.client.InfluxDBClient;
import com.kraken.storage.client.StorageClient;
import com.kraken.storage.entity.StorageNode;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor
@Component
class SpringAnalysisService implements AnalysisService {

  private static final String RESULT_JSON = "result.json";

  @NonNull AnalysisProperties properties;
  @NonNull GrafanaClientProperties grafanaClientProperties;

  @NonNull StorageClient storageClient;
  @NonNull GrafanaClient grafanaClient;
  @NonNull InfluxDBClient influxdbClient;

  @NonNull Function<List<HttpHeader>, String> headersToExtension;
  @NonNull Function<ResultStatus, Long> statusToEndDate;

  @Override
  public Mono<StorageNode> create(final Result result) {
    final var resultPath = properties.getResultPath(result.getId());
    final var resultJsonPath = resultPath.resolve(RESULT_JSON).toString();

    final var createGrafanaReport = storageClient.getContent(grafanaClientProperties.getGrafanaDashboard())
        .map(dashboard -> grafanaClient.initDashboard(result.getId(), result.getDescription() + " - " + result.getId(), result.getStartDate(), dashboard))
        .flatMap(grafanaClient::importDashboard);

    final var createGrafanaReportOrNot = Mono.just(result).flatMap(res -> res.getType().isDebug() ? Mono.just("ok") : createGrafanaReport);

    return createGrafanaReportOrNot.flatMap(s -> storageClient.createFolder(resultPath.toString()))
        .flatMap(storageNode -> storageClient.setJsonContent(resultJsonPath, result));
  }

  @Override
  public Mono<String> delete(final String resultId) {
    final var resultPath = properties.getResultPath(resultId);
    final var resultJsonPath = resultPath.resolve(RESULT_JSON).toString();
    final var deleteFolder = storageClient.delete(resultPath.toString());
    final var getResult = storageClient.getJsonContent(resultJsonPath, Result.class);
    final var deleteReport = getResult.flatMap(result -> result.getType().isDebug() ? Mono.just("ok") : Mono.zip(grafanaClient.deleteDashboard(resultId), influxdbClient.deleteSeries(resultId)));
    return Mono.zip(deleteFolder, deleteReport).map(objects -> resultId);
  }

  @Override
  public Mono<StorageNode> setStatus(final String resultId, final ResultStatus status) {
    final var endDate = this.statusToEndDate.apply(status);
    final var resultPath = properties.getResultPath(resultId).resolve(RESULT_JSON).toString();

    return storageClient.getJsonContent(resultPath, Result.class)
        .flatMap(result -> {
          if (result.getStatus().isTerminal()) {
            return Mono.error(new IllegalArgumentException(String.format("Result %s is already in state %s and thus cannot be changed.", resultId, result.getStatus().toString())));
          }
          if (result.getType().isDebug()) {
            return Mono.just(result);
          }
          return grafanaClient.getDashboard(resultId)
              .map(dashboard -> grafanaClient.updatedDashboard(endDate, dashboard))
              .flatMap(grafanaClient::setDashboard)
              .map(s -> result);
        })
        .map(result -> result.withEndDate(endDate).withStatus(status))
        .flatMap(result -> storageClient.setJsonContent(resultPath, result));
  }

  @Override
  public Mono<DebugEntry> addDebug(final DebugEntry debug) {
    final var outputFolder = properties.getResultPath(debug.getResultId());

    return Mono.just(debug)
        .flatMap(debugEntry -> {
          if (!debug.getRequestBodyFile().isEmpty()) {
            final var body = debug.getRequestBodyFile();
            final var bodyFile = String.format("%s-request%s", debugEntry.getId(), this.headersToExtension.apply(debugEntry.getRequestHeaders()));
            return storageClient.setContent(outputFolder.resolve(bodyFile).toString(), body).map(s -> debugEntry.withRequestBodyFile(bodyFile));
          }
          return Mono.just(debugEntry);
        })
        .flatMap(debugEntry -> {
          if (!debug.getResponseBodyFile().isEmpty()) {
            final var body = debug.getResponseBodyFile();
            final var bodyFile = String.format("%s-response%s", debugEntry.getId(), this.headersToExtension.apply(debugEntry.getResponseHeaders()));
            return storageClient.setContent(outputFolder.resolve(bodyFile).toString(), body).map(s -> debugEntry.withResponseBodyFile(bodyFile));
          }
          return Mono.just(debugEntry);
        })
        .flatMap(debugEntry -> storageClient.setJsonContent(outputFolder.resolve(debugEntry.getId() + ".debug").toString(), debugEntry).map(storageNode -> debugEntry));
  }

}

