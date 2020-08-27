package com.octoperf.kraken.analysis.service.spring;

import com.octoperf.kraken.analysis.entity.DebugEntry;
import com.octoperf.kraken.analysis.entity.Result;
import com.octoperf.kraken.analysis.entity.ResultStatus;
import com.octoperf.kraken.analysis.service.api.AnalysisService;
import com.octoperf.kraken.analysis.service.api.HeadersToExtension;
import com.octoperf.kraken.analysis.service.api.StatusToEndDate;
import com.octoperf.kraken.config.grafana.api.AnalysisResultsProperties;
import com.octoperf.kraken.config.grafana.api.GrafanaProperties;
import com.octoperf.kraken.grafana.client.api.GrafanaUserClient;
import com.octoperf.kraken.grafana.client.api.GrafanaUserClientBuilder;
import com.octoperf.kraken.grafana.client.api.GrafanaUserConverter;
import com.octoperf.kraken.influxdb.client.api.InfluxDBClientBuilder;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUserConverter;
import com.octoperf.kraken.security.admin.client.api.SecurityAdminClient;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.client.api.StorageClientBuilder;
import com.octoperf.kraken.storage.entity.StorageNode;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
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

  @NonNull InfluxDBClientBuilder influxDBClientBuilder;
  @NonNull InfluxDBUserConverter influxDBUserConverter;
  @NonNull GrafanaUserClientBuilder grafanaUserClientBuilder;
  @NonNull GrafanaUserConverter grafanaUserConverter;
  @NonNull StorageClientBuilder storageClientBuilder;
  @NonNull Mono<SecurityAdminClient> securityAdminClient;

  @NonNull HeadersToExtension headersToExtension;
  @NonNull StatusToEndDate statusToEndDate;

  @Override
  public Mono<StorageNode> createResult(final Owner owner, final Result result) {
    final var storageClientMono = this.getImpersonateStorage(owner);
    return storageClientMono.flatMap(storageClient -> {
      final var resultPath = resultsProperties.getResultPath(result.getId());
      final var resultJsonPath = resultPath.resolve(RESULT_JSON).toString();
      log.info(String.format("Create result at %s", resultJsonPath));

      final var createGrafanaReport = this.getGrafanaUserClient(owner)
          .flatMap(client -> storageClient.getContent(grafanaProperties.getDashboard())
              .flatMap(dashboard -> client.getFolderId(owner.getProjectId())
                  .flatMap(folderId -> client.importDashboard(result.getId(), folderId, result.getDescription() + " - " + result.getId(), result.getStartDate(), dashboard))));

      final var createGrafanaReportOrNot = Mono.just(result).flatMap(res -> res.getType().isDebug() ? Mono.just("ok") : createGrafanaReport);

      return storageClient.setJsonContent(resultJsonPath, result)
          .flatMap(storageNode -> createGrafanaReportOrNot.map(s -> storageNode));
    });
  }

  @Override
  public Mono<String> deleteResult(final Owner owner, final String resultId) {
    final var storageClientMono = this.getImpersonateStorage(owner);
    return storageClientMono.flatMap(storageClient -> {
      final var resultPath = resultsProperties.getResultPath(resultId);
      final var resultJsonPath = resultPath.resolve(RESULT_JSON).toString();
      final var deleteFolder = storageClient.delete(resultPath.toString());
      final var getResult = storageClient.getJsonContent(resultJsonPath, Result.class);
      final var deleteRunReport = securityAdminClient.flatMap(client -> client.getUser(owner.getUserId())).flatMap(krakenUser -> {
        final var grafanaUser = grafanaUserConverter.apply(krakenUser);
        final var influxDBUser = influxDBUserConverter.apply(krakenUser);
        final var deleteDashboard = grafanaUserClientBuilder.build(grafanaUser, influxDBUser)
            .flatMap(client -> client.deleteDashboard(resultId));
        final var deleteSeries = influxDBClientBuilder.build().flatMap(client -> client.deleteSeries(influxDBUser.getDatabase(), resultId));
        return Mono.zip(deleteDashboard, deleteSeries);
      });

      final var deleteReport = getResult.flatMap(result -> result.getType().isDebug() ? Mono.just("ok") : deleteRunReport);
      return deleteReport.then(deleteFolder).map(objects -> resultId);
    });
  }

  @Override
  public Mono<StorageNode> setResultStatus(final Owner owner, final String resultId, final ResultStatus status) {
    final var storageClientMono = this.getImpersonateStorage(owner);
    final var endDate = this.statusToEndDate.apply(status);
    final var resultPath = resultsProperties.getResultPath(resultId).resolve(RESULT_JSON).toString();

    return storageClientMono.flatMap(storageClient ->
        storageClient.getJsonContent(resultPath, Result.class)
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
                  .flatMap(client -> client.getFolderId(owner.getProjectId())
                      .flatMap(folderId -> client.updateDashboard(resultId, folderId, endDate)))
                  .map(s -> result);
            })
            .map(result -> result.withEndDate(endDate).withStatus(status))
            .flatMap(result -> storageClient.setJsonContent(resultPath, result))
    );
  }

  @Override
  public Mono<DebugEntry> addDebugEntry(final Owner owner, final DebugEntry debug) {
    final var storageClientMono = this.getSessionStorage(owner);
    final var outputFolder = resultsProperties.getResultPath(debug.getResultId()).resolve("debug");

    return storageClientMono.flatMap(storageClient ->
        Mono.just(debug)
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
            .flatMap(debugEntry -> storageClient.setJsonContent(outputFolder.resolve(debugEntry.getId() + ".debug").toString(), debugEntry).map(storageNode -> debugEntry))
    );
  }

  @Override
  public Mono<ResponseCookie> grafanaLogin(final Owner owner) {
    return securityAdminClient.flatMap(client -> client.getUser(owner.getUserId()))
        .map(grafanaUserConverter::apply)
        .flatMap(grafanaUserClientBuilder::getSessionCookie);
  }

  private Mono<GrafanaUserClient> getGrafanaUserClient(final Owner owner) {
    return securityAdminClient.flatMap(client -> client.getUser(owner.getUserId()))
        .map(krakenUser -> Tuples.of(grafanaUserConverter.apply(krakenUser), influxDBUserConverter.apply(krakenUser)))
        .flatMap(t2 -> grafanaUserClientBuilder.build(t2.getT1(), t2.getT2()));
  }

  private Mono<StorageClient> getSessionStorage(final Owner owner) {
    return storageClientBuilder
        .build(AuthenticatedClientBuildOrder.builder()
            .mode(AuthenticationMode.SESSION)
            .applicationId(owner.getApplicationId())
            .projectId(owner.getProjectId())
            .build());
  }

  private Mono<StorageClient> getImpersonateStorage(final Owner owner) {
    log.info(String.format("Impersonate owner %s for storage access", owner.toString()));
    return storageClientBuilder
        .build(AuthenticatedClientBuildOrder.builder()
            .mode(AuthenticationMode.IMPERSONATE)
            .userId(owner.getUserId())
            .applicationId(owner.getApplicationId())
            .projectId(owner.getProjectId())
            .build());
  }

}

