package com.kraken.analysis.rest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.AnalysisProperties;
import com.kraken.analysis.RunProperties;
import com.kraken.commons.analysis.entity.Result;
import com.kraken.commons.analysis.entity.ResultStatus;
import com.kraken.commons.analysis.entity.ResultType;
import com.kraken.commons.command.client.CommandClient;
import com.kraken.commons.command.entity.Command;
import com.kraken.commons.gatling.properties.GatlingProperties;
import com.kraken.commons.grafana.client.GrafanaClient;
import com.kraken.commons.grafana.client.GrafanaClientProperties;
import com.kraken.commons.influxdb.client.InfluxDBClient;
import com.kraken.commons.influxdb.client.InfluxDBClientProperties;
import com.kraken.commons.storage.client.StorageClient;
import com.kraken.commons.storage.entity.StorageNode;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static com.kraken.commons.analysis.entity.ResultType.DEBUG;
import static com.kraken.commons.analysis.entity.ResultType.RUN;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Component
class AnalysisService {

  private static final String RESULT_JSON = "result.json";

  AnalysisProperties properties;
  InfluxDBClientProperties influxDBClientProperties;
  GrafanaClientProperties grafanaClientProperties;
  GatlingProperties gatlingProperties;

  StorageClient storageClient;
  CommandClient commandClient;
  GrafanaClient grafanaClient;
  InfluxDBClient influxdbClient;

  Map<ResultStatus, Supplier<Long>> statusToEndDate;


  AnalysisService(final AnalysisProperties properties,
                  final InfluxDBClientProperties influxDBClientProperties,
                  final GrafanaClientProperties grafanaClientProperties,
                  final GatlingProperties gatlingProperties,
                  final StorageClient storageClient,
                  final CommandClient commandClient,
                  final GrafanaClient grafanaClient,
                  final InfluxDBClient influxdbClient) {
    super();
    this.properties = requireNonNull(properties);
    this.influxDBClientProperties = requireNonNull(influxDBClientProperties);
    this.grafanaClientProperties = requireNonNull(grafanaClientProperties);
    this.gatlingProperties = requireNonNull(gatlingProperties);
    this.storageClient = requireNonNull(storageClient);
    this.commandClient = requireNonNull(commandClient);
    this.grafanaClient = requireNonNull(grafanaClient);
    this.influxdbClient = requireNonNull(influxdbClient);

    final Supplier<Long> endDateNow = () -> new Date().getTime();
    this.statusToEndDate = ImmutableMap.of(ResultStatus.RUNNING, () -> 0L,
        ResultStatus.COMPLETED, endDateNow,
        ResultStatus.CANCELED, endDateNow,
        ResultStatus.FAILED, endDateNow);
  }

  private Mono<String> start(final String applicationId,
                             final String runDescription,
                             final String testId,
                             final Long startDate,
                             final Map<String, String> environment,
                             final ResultType type,
                             final RunProperties runProperties) {
    final var result = Result.builder()
        .id(testId)
        .runDescription(runDescription)
        .status(ResultStatus.STARTING)
        .endDate(0L)
        .startDate(startDate)
        .type(type)
        .build();

    final var envBuilder = ImmutableMap.<String, String>builder();
    envBuilder.putAll(environment);
    envBuilder.put("KRAKEN_TEST_ID", testId);
    envBuilder.put("KRAKEN_VERSION", gatlingProperties.getVersion());
    envBuilder.put("KRAKEN_ANALYSIS_URL", properties.getAnalysisUrl());
    envBuilder.put("KRAKEN_INFLUXDB_URL", influxDBClientProperties.getInfluxdbDockerUrl());
    envBuilder.put("KRAKEN_INFLUXDB_DATABASE", influxDBClientProperties.getInfluxdbDatabase());
    envBuilder.put("KRAKEN_INFLUXDB_USER", influxDBClientProperties.getInfluxdbUser());
    envBuilder.put("KRAKEN_INFLUXDB_PASSWORD", influxDBClientProperties.getInfluxdbPassword());

    final var command = Command.builder()
        .id("")
        .applicationId(applicationId)
        .path(runProperties.getRoot())
        .command(Arrays.asList("/bin/sh", "-c", String.format("chmod +x %s && ./%s", runProperties.getScript(), runProperties.getScript())))
        .onCancel(Arrays.asList("/bin/sh", "-c", String.format("chmod +x %s && ./%s", runProperties.getCancelScript(), runProperties.getCancelScript())))
        .environment(envBuilder.build())
        .build();
    log.debug(command.toString());

    return storageClient.createFolder(gatlingProperties.getTestResultPath(testId).toString())
        .flatMap(storageNode -> storageClient.setJsonContent(Paths.get(storageNode.getPath(), RESULT_JSON).toString(), result))
        .flatMap(storageNode -> commandClient.execute(command));
  }

  Mono<String> record(final String applicationId,
                      final Map<String, String> environment) {

    final var testId = UUID.randomUUID().toString();
    final var envBuilder = ImmutableMap.<String, String>builder();
    envBuilder.putAll(environment);
    envBuilder.put("KRAKEN_TEST_ID", testId);
    envBuilder.put("KRAKEN_VERSION", gatlingProperties.getVersion());

    final var command = Command.builder()
        .id("")
        .applicationId(applicationId)
        .path(properties.getRecordProperties().getRoot())
        .command(Arrays.asList("/bin/sh", "-c", String.format("chmod +x %s && ./%s", properties.getRecordProperties().getScript(), properties.getRecordProperties().getScript())))
        .onCancel(ImmutableList.of())
        .environment(envBuilder.build())
        .build();
    log.debug(command.toString());
    return commandClient.execute(command);
  }

  Mono<String> debug(final String applicationId,
                     final String runDescription,
                     final Map<String, String> environment) {
    final var testId = UUID.randomUUID().toString();
    final var startDate = new Date().getTime();

    return this.start(applicationId, runDescription, testId, startDate, environment, DEBUG, properties.getDebugProperties());
  }

  Mono<String> run(final String applicationId,
                   final String runDescription,
                   final Map<String, String> environment) {
    final var testId = UUID.randomUUID().toString();
    final var startDate = new Date().getTime();
    final var startTest = this.start(applicationId, runDescription, testId, startDate, environment, RUN, properties.getRunProperties());

    final var createGrafanaReport = storageClient.getContent(grafanaClientProperties.getGrafanaDashboard())
        .map(dashboard -> grafanaClient.initDashboard(testId, runDescription + " - " + testId, startDate, dashboard))
        .flatMap(grafanaClient::importDashboard);

    return Mono.zip(startTest, createGrafanaReport).map(Tuple2::getT1);
  }

  Mono<String> delete(final String testId) {
    final var resultPath = gatlingProperties.getTestResultPath(testId).resolve(RESULT_JSON).toString();
    final var deleteFolder = storageClient.delete(gatlingProperties.getTestResultPath(testId).toString());
    final var getResult = storageClient.getJsonContent(resultPath, Result.class);
    final var deleteReport = getResult.flatMap(result -> result.getType().isDebug() ? Mono.just("ok") : Mono.zip(grafanaClient.deleteDashboard(testId), influxdbClient.deleteSeries(testId)));
    return Mono.zip(deleteFolder, deleteReport).map(objects -> testId);
  }

  Mono<StorageNode> setStatus(final String testId, final ResultStatus status) {
    final var endDate = this.statusToEndDate.get(status).get();
    final var resultPath = gatlingProperties.getTestResultPath(testId).resolve(RESULT_JSON).toString();

    return storageClient.getJsonContent(resultPath, Result.class)
        .flatMap(result -> {
          if (result.getType().isDebug()) {
            return Mono.just(result);
          } else {
            return grafanaClient.getDashboard(testId)
                .map(dashboard -> grafanaClient.updatedDashboard(endDate, dashboard))
                .flatMap(grafanaClient::setDashboard)
                .map(s -> result);
          }
        })
        .map(result -> result.withEndDate(endDate).withStatus(status))
        .flatMap(result -> storageClient.setJsonContent(resultPath, result));
  }

}

