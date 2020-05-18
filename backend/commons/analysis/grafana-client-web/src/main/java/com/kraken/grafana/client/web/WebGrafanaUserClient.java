package com.kraken.grafana.client.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.kraken.config.influxdb.api.InfluxDBProperties;
import com.kraken.grafana.client.api.GrafanaUser;
import com.kraken.grafana.client.api.GrafanaUserClient;
import com.kraken.influxdb.client.api.InfluxDBUser;
import com.kraken.template.api.TemplateService;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.Function;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class WebGrafanaUserClient implements GrafanaUserClient {

  GrafanaUser grafanaUser;
  InfluxDBUser influxDBUser;
  WebClient webClient;
  ObjectMapper mapper;
  SimpleDateFormat format;
  InfluxDBProperties dbProperties;
  TemplateService templateService;

  WebGrafanaUserClient(@NonNull final GrafanaUser grafanaUser,
                       @NonNull final InfluxDBUser influxDBUser,
                       @NonNull final WebClient webClient,
                       @NonNull final InfluxDBProperties dbProperties,
                       @NonNull final ObjectMapper mapper,
                       @NonNull final TemplateService templateService) {
    this.grafanaUser = grafanaUser;
    this.influxDBUser = influxDBUser;
    this.dbProperties = dbProperties;
    this.webClient = webClient;
    this.mapper = mapper;
    this.templateService = templateService;
    //  2019-03-22T10:01:00.000Z
    this.format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    format.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  @Override
  public Mono<Long> createDatasource() {
    final var createDatasource = retry(webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/api/datasources").build())
        .body(BodyInserters.fromValue(CreateGrafanaDatasourceRequest.builder()
            .name(grafanaUser.getDatasourceName())
            .access("proxy")
            .type("influxdb")
            .isDefault(true)
            .build()))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log);

    return createDatasource
        .flatMap(this::updateDatasource)
        .flatMap(t2 -> this.putDatasource(t2.getT1(), t2.getT2()));
  }

  private Mono<Long> putDatasource(final Long datasourceId, final String datasourcePayload) {
    return Mono.defer(() -> retry(webClient.put()
        .uri(uriBuilder -> uriBuilder.path("/api/datasources/{id}").build(datasourceId))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(BodyInserters.fromValue(datasourcePayload))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log)).map(s -> datasourceId);
  }

  private Mono<Tuple2<Long, String>> updateDatasource(final String datasource) {
    return Mono.fromCallable(() -> {
      final JsonNode responseNode = mapper.readTree(datasource);
      final ObjectNode datasourceNode = (ObjectNode) responseNode.get("datasource");
      datasourceNode.put("url", dbProperties.getUrl());
      datasourceNode.put("user", influxDBUser.getUsername());
      datasourceNode.put("database", influxDBUser.getDatabase());

      final ObjectNode jsonDataNode = (ObjectNode) datasourceNode.get("jsonData");
      jsonDataNode.put("httpMode", "POST");

      final ObjectNode secureJsonDataNode = mapper.createObjectNode();
      secureJsonDataNode.put("password", influxDBUser.getPassword());
      datasourceNode.set("secureJsonData", secureJsonDataNode);
      final var updated = mapper.writeValueAsString(datasourceNode);
      final var id = datasourceNode.get("id").asLong();
      return Tuples.of(id, updated);
    });
  }

  @Override
  public Mono<String> importDashboard(final String testId,
                                      final String title,
                                      final Long startDate,
                                      final String dashboard) {
    return this.initDashboard(testId, title, startDate, dashboard)
        .flatMap(this::importDashboard);
  }

  @Override
  public Mono<String> updateDashboard(final String testId, final Long endDate) {
    return this.getDashboard(testId)
        .flatMap(dashboard -> this.updatedDashboard(endDate, dashboard))
        .flatMap(this::setDashboard)
        .doOnSubscribe(subscription -> log.info(String.format("Update dashboard for test %s", testId)))
        .doOnNext(dashboard -> log.info(String.format("Updated dashboard for test %s", testId)));
  }

  @Override
  public Mono<String> deleteDashboard(final String testId) {
    return retry(webClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/api/dashboards/uid/{testId}").build(testId))
        .retrieve()
        .bodyToMono(String.class), log);
  }

  private Mono<String> getDashboard(final String testId) {
    return retry(webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/api/dashboards/uid/{testId}").build(testId))
        .retrieve()
        .bodyToMono(String.class)
        .flatMap(this::decapsulateDashboard), log);
  }

  private Mono<String> setDashboard(final String dashboard) {
    return encapsulateSetDashboard(dashboard)
        .flatMap(encapsulated -> retry(webClient.post()
            .uri(uriBuilder -> uriBuilder.path("/api/dashboards/db").build())
            .body(BodyInserters.fromValue(encapsulated))
            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(String.class), log));
  }

  private Mono<String> importDashboard(final String dashboard) {
    return encapsulateImportDashboard(dashboard)
        .flatMap(encapsulated -> retry(webClient.post()
            .uri(uriBuilder -> uriBuilder.path("/api/dashboards/import").build())
            .body(BodyInserters.fromValue(encapsulated))
            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(String.class), log))
        .doOnSubscribe(subscription -> log.info("Importing dashboard"))
        .doOnNext(tpl -> log.info("Imported dashboard"));
  }

  private Mono<String> encapsulateSetDashboard(final String dashboard) {
    return Mono.fromCallable(() -> {
      final JsonNode dashboardNode = mapper.readTree(dashboard);
      // Dashboard must be encapsulated in another object when updating value
      final ObjectNode setNode = mapper.createObjectNode();
      setNode.set("dashboard", dashboardNode);
      setNode.put("overwrite", false);
      setNode.put("message", this.format.format(new Date()));
      return mapper.writeValueAsString(setNode);
    });
  }

  private Mono<String> decapsulateDashboard(final String dashboardResult) {
    return Mono.fromCallable(() -> {
      final JsonNode dashboardResultNode = mapper.readTree(dashboardResult);
      // Dashboard must be decapsulated when received from grafana server
      return mapper.writeValueAsString(dashboardResultNode.get("dashboard"));
    });
  }

  private Mono<String> initDashboard(final String testId,
                                     final String title,
                                     final Long startDate,
                                     final String dashboard) {
    final var contextBuilder = ImmutableMap.<String, String>builder();
    contextBuilder.put("ID", "null");
    contextBuilder.put("VERSION", "1");
    contextBuilder.put("REFRESH", "1s");
    contextBuilder.put("TIMEZONE", "utc");
    contextBuilder.put("HIDDEN", "true");
    contextBuilder.put("TIME_PICKER_HIDDEN", "true");
    contextBuilder.put("TEST_ID", testId);
    contextBuilder.put("TITLE", title);
    contextBuilder.put("START_DATE", this.format.format(new Date(startDate)));
    contextBuilder.put("END_DATE", "now");
    contextBuilder.put("DATABASE_NAME", influxDBUser.getDatabase());
    contextBuilder.put("DATASOURCE_NAME", grafanaUser.getDatasourceName());

    return templateService.replaceAll(dashboard, contextBuilder.build())
        .doOnSubscribe(subscription -> log.info(String.format("Initializing dashboard for test %s", testId)))
        .doOnNext(tpl -> log.info(String.format("Initialized dashboard for test %s", testId)))
        .doOnNext(log::debug);
  }

  private Mono<String> updatedDashboard(final Long endDate,
                                        final String dashboard) {
    return Mono.fromCallable(() -> {
      final JsonNode node = mapper.readTree(dashboard);
      final ObjectNode objectNode = ((ObjectNode) node);

      final boolean refresh = endDate == 0L;
      final ObjectNode timeNode = ((ObjectNode) node.get("time"));
      if (refresh) {
        objectNode.put("refresh", "1s");
        timeNode.put("to", "now");
      } else {
        objectNode.put("refresh", false);
        timeNode.put("to", this.format.format(new Date(endDate)));
      }
      return mapper.writeValueAsString(node);
    });
  }

  private Mono<String> encapsulateImportDashboard(final String dashboard) {
    return Mono.fromCallable(() -> {
      final JsonNode dashboardNode = mapper.readTree(dashboard);
      // Dashboard must be encapsulated in another object when importing
      final ObjectNode importNode = mapper.createObjectNode();
      importNode.set("dashboard", dashboardNode);
      importNode.put("overwrite", true);
      importNode.set("inputs", mapper.createArrayNode());
      importNode.put("folderId", 0);
      return mapper.writeValueAsString(importNode);
    });
  }

}
