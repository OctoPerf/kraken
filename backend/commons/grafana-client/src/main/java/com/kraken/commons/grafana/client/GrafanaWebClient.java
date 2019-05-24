package com.kraken.commons.grafana.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Component
class GrafanaWebClient implements GrafanaClient{

  WebClient webClient;
  ObjectMapper mapper;
  SimpleDateFormat format;

  GrafanaWebClient(@Qualifier("webClientGrafana") final WebClient webClient, final ObjectMapper mapper) {
    this.webClient = Objects.requireNonNull(webClient);
    this.mapper = Objects.requireNonNull(mapper);
    //  2019-03-22T10:01:00.000Z
    this.format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    format.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  public Mono<String> getDashboard(final String testId) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/api/dashboards/uid/{testId}").build(testId))
        .retrieve()
        .bodyToMono(String.class)
        .map(this::decapsulateDashboard);
  }

  public Mono<String> setDashboard(final String dashboard) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/api/dashboards/db").build())
        .body(BodyInserters.fromObject(encapsulateSetDashboard(dashboard)))
        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(String.class);
  }

  public  Mono<String> importDashboard(final String dashboard) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/api/dashboards/import").build())
        .body(BodyInserters.fromObject(encapsulateImportDashboard(dashboard)))
        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(String.class);
  }

  public Mono<String> deleteDashboard(final String testId) {
    return webClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/api/dashboards/uid/{testId}").build(testId))
        .retrieve()
        .bodyToMono(String.class);
  }

  private String encapsulateSetDashboard(final String dashboard) {
    try {
      final JsonNode dashboardNode = mapper.readTree(dashboard);
      // Dashboard must be encapsulated in another object when updating value
      final ObjectNode setNode = mapper.createObjectNode();
      setNode.set("dashboard", dashboardNode);
      setNode.put("overwrite", false);
      setNode.put("message", this.format.format(new Date()));
      return mapper.writeValueAsString(setNode);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String decapsulateDashboard(final String dashboardResult) {
    try {
      final JsonNode dashboardResultNode = mapper.readTree(dashboardResult);
      // Dashboard must be decapsulated when received from grafana server
      return mapper.writeValueAsString(dashboardResultNode.get("dashboard"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String initDashboard(final String testId,
                       final String title,
                       final Long startDate,
                       final String dashboard) {

    try {
      final ObjectNode dashboardNode = ((ObjectNode) mapper.readTree(dashboard));
      // Id must be nullified when importing a dashboard
      dashboardNode.set("id", NullNode.getInstance());
      dashboardNode.put("uid", testId);
      // Ensure title is unique
      dashboardNode.put("title", title);
      dashboardNode.put("version", 1);
      dashboardNode.put("refresh", "1s");
      dashboardNode.put("timezone", "utc");

      final ObjectNode timeNode = ((ObjectNode) dashboardNode.get("time"));
      timeNode.put("from", this.format.format(new Date(startDate)));
      timeNode.put("to", "now");

      final ObjectNode timePickerNode = ((ObjectNode) dashboardNode.get("timepicker"));
      timePickerNode.put("hidden", true);

      final ObjectNode testVariableNode = ((ObjectNode) dashboardNode.get("templating").get("list").get(1));
      testVariableNode.put("query", testId);
      final ObjectNode currentNode = ((ObjectNode) testVariableNode.get("current"));
      currentNode.put("text", testId);
      currentNode.put("value", testId);
      final ObjectNode optionsNode = ((ObjectNode) testVariableNode.get("options").get(0));
      optionsNode.put("text", testId);
      optionsNode.put("value", testId);

      return mapper.writeValueAsString(dashboardNode);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String updatedDashboard(final Long endDate,
                          final String dashboard) {
    try {
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
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String encapsulateImportDashboard(final String dashboard) {
    try {
      final JsonNode dashboardNode = mapper.readTree(dashboard);
      // Dashboard must be encapsulated in another object when importing
      final ObjectNode importNode = mapper.createObjectNode();
      importNode.set("dashboard", dashboardNode);
      importNode.put("overwrite", true);
      importNode.set("inputs", mapper.createArrayNode());
      importNode.put("folderId", 0);

      return mapper.writeValueAsString(importNode);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
