package com.kraken.analysis.server.service;

import com.kraken.analysis.entity.DebugEntry;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.config.grafana.api.AnalysisResultsProperties;
import com.kraken.config.grafana.api.GrafanaProperties;
import com.kraken.grafana.client.api.*;
import com.kraken.influxdb.client.api.InfluxDBClient;
import com.kraken.influxdb.client.api.InfluxDBUserConverter;
import com.kraken.security.admin.client.api.SecurityAdminClient;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.security.entity.functions.api.OwnerToApplicationId;
import com.kraken.security.entity.functions.api.OwnerToUserId;
import com.kraken.security.entity.owner.Owner;
import com.kraken.storage.client.api.StorageClient;
import com.kraken.storage.client.api.StorageClientBuilder;
import com.kraken.storage.entity.StorageNode;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class SpringAnalysisService implements AnalysisService {

  private static final String RESULT_JSON = "result.json";

  @NonNull AnalysisResultsProperties resultsProperties;
  @NonNull GrafanaProperties grafanaProperties;

  @NonNull InfluxDBClient influxdbClient;
  @NonNull InfluxDBUserConverter influxDBUserConverter;
  @NonNull GrafanaUserClientBuilder grafanaUserClientBuilder;
  @NonNull GrafanaUserConverter grafanaUserConverter;
  @NonNull StorageClientBuilder storageClientBuilder;
  @NonNull SecurityAdminClient securityAdminClient;

  @NonNull OwnerToApplicationId toApplicationId;
  @NonNull OwnerToUserId toUserId;

  @NonNull HeadersToExtension headersToExtension;
  @NonNull StatusToEndDate statusToEndDate;

  @Override
  public Mono<StorageNode> create(final Owner owner, final Result result) {
    final var storageClient = this.getImpersonateStorage(owner);
    final var resultPath = resultsProperties.getResultPath(result.getId());
    final var resultJsonPath = resultPath.resolve(RESULT_JSON).toString();

    final var createGrafanaReport = this.getGrafanaUserClient(owner)
        .flatMap(client -> storageClient.getContent(grafanaProperties.getDashboard()).map(dashboard -> Tuples.of(client, dashboard)))
        .flatMap(t2 -> t2.getT1().importDashboard(result.getId(), result.getDescription() + " - " + result.getId(), result.getStartDate(), t2.getT2()));

    final var createGrafanaReportOrNot = Mono.just(result).flatMap(res -> res.getType().isDebug() ? Mono.just("ok") : createGrafanaReport);

    return storageClient.createFolder(resultPath.toString())
        .flatMap(storageNode -> storageClient.setJsonContent(resultJsonPath, result))
        .flatMap(storageNode -> createGrafanaReportOrNot.map(s -> storageNode));
  }

  @Override
  public Mono<String> delete(final Owner owner, final String resultId) {
    final var storageClient = this.getImpersonateStorage(owner);
    final var resultPath = resultsProperties.getResultPath(resultId);
    final var resultJsonPath = resultPath.resolve(RESULT_JSON).toString();
    final var deleteFolder = storageClient.delete(resultPath.toString());
    final var getResult = storageClient.getJsonContent(resultJsonPath, Result.class);
    final var deleteRunReport = securityAdminClient.getUser(toUserId.apply(owner).orElseThrow()).flatMap(krakenUser -> {
      final var grafanaUser = grafanaUserConverter.apply(krakenUser);
      final var influxDBUser = influxDBUserConverter.apply(krakenUser);
      final var deleteDashboard = grafanaUserClientBuilder.grafanaUser(grafanaUser).influxDBUser(influxDBUser).build()
          .map(client -> client.deleteDashboard(resultId));
      final var deleteSeries = influxdbClient.deleteSeries(influxDBUser.getDatabase(), resultId);
      return Mono.zip(deleteDashboard, deleteSeries);
    });

    final var deleteReport = getResult.flatMap(result -> result.getType().isDebug() ? Mono.just("ok") : deleteRunReport);
    return Mono.zip(deleteFolder, deleteReport).map(objects -> resultId);
  }

  @Override
  public Mono<StorageNode> setStatus(final Owner owner, final String resultId, final ResultStatus status) {
    final var storageClient = this.getImpersonateStorage(owner);
    final var endDate = this.statusToEndDate.apply(status);
    final var resultPath = resultsProperties.getResultPath(resultId).resolve(RESULT_JSON).toString();

    return storageClient.getJsonContent(resultPath, Result.class)
        .map(result -> {
          log.info(String.format("Previous status for %s is %s", resultId, result.getStatus()));
          return result;
        })
        .filter(result -> !result.getStatus().isTerminal())
        .flatMap(result -> {
          log.info(String.format("Result type for %s is %s", resultId, result.getType()));
          if (result.getType().isDebug()) {
            return Mono.just(result);
          }
          return this.getGrafanaUserClient(owner)
              .flatMap(client -> client.updateDashboard(resultId, endDate))
              .map(s -> result);
        })
        .map(result -> result.withEndDate(endDate).withStatus(status))
        .flatMap(result -> storageClient.setJsonContent(resultPath, result));
  }

  @Override
  public Mono<DebugEntry> addDebug(final Owner owner, final DebugEntry debug) {
    final var storageClient = this.getSessionStorage(owner);
    final var outputFolder = resultsProperties.getResultPath(debug.getResultId()).resolve("debug");

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

  @Override
  public Mono<ResponseCookie> grafanaLogin(final Owner owner) {
    return getGrafanaUserClientBuilder(owner).flatMap(GrafanaUserClientBuilder::getSessionCookie);
  }

  private Mono<GrafanaUserClient> getGrafanaUserClient(final Owner owner) {
    return this.getGrafanaUserClientBuilder(owner).flatMap(GrafanaUserClientBuilder::build);
  }

  private Mono<GrafanaUserClientBuilder> getGrafanaUserClientBuilder(final Owner owner) {
    return securityAdminClient.getUser(toUserId.apply(owner).orElseThrow())
        .map(user -> grafanaUserClientBuilder.grafanaUser(grafanaUserConverter.apply(user)).influxDBUser(influxDBUserConverter.apply(user)));
  }

  private StorageClient getSessionStorage(final Owner owner) {
    final var ids = ownerToIds(owner);
    return storageClientBuilder.mode(AuthenticationMode.SESSION).applicationId(ids.getT1()).build();
  }

  private StorageClient getImpersonateStorage(final Owner owner) {
    final var ids = ownerToIds(owner);
    return storageClientBuilder.mode(AuthenticationMode.IMPERSONATE, ids.getT2()).applicationId(ids.getT1()).build();
  }

  private Tuple2<String, String> ownerToIds(final Owner owner) {
    final var applicationId = toApplicationId.apply(owner).orElseThrow();
    final var userId = toUserId.apply(owner).orElseThrow();
    return Tuples.of(applicationId, userId);
  }
}

