package com.kraken.commons.grafana.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.kraken.test.utils.ResourceUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class GrafanaClientTest {

  private ObjectMapper mapper;
  private MockWebServer grafanaMockWebServer;
  private GrafanaClient client;

  @Before
  public void before() {
    mapper = new ObjectMapper();
    grafanaMockWebServer = new MockWebServer();
    client = new GrafanaWebClient(WebClient.create(grafanaMockWebServer.url("/").toString()), mapper);
  }

  @After
  public void tearDown() throws IOException {
    grafanaMockWebServer.shutdown();
  }

  @Test
  public void shouldGetDashboard() throws InterruptedException {
    final var id = "id";
    final var dashboard = "{\"refresh\":\"1s\"}";

    grafanaMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("{\"dashboard\":{\"refresh\":\"1s\"}}")
    );

    final var response = client.getDashboard(id).block();
    assertThat(response).isEqualTo(dashboard);

    final RecordedRequest commandRequest = grafanaMockWebServer.takeRequest();
    assertThat(commandRequest.getPath()).isEqualTo("/api/dashboards/uid/id");
  }

  @Test
  public void shouldUpdateDashboard() throws InterruptedException, IOException {
    final var dashboard = "{\"refresh\":false}";
    final var setDashboardResponse = "response";

    grafanaMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(setDashboardResponse)
    );

    final var response = client.setDashboard(dashboard).block();
    assertThat(response).isEqualTo(setDashboardResponse);

    final RecordedRequest commandRequest = grafanaMockWebServer.takeRequest();
    assertThat(commandRequest.getPath()).isEqualTo("/api/dashboards/db");
    final var node = mapper.readTree(commandRequest.getBody().readString(Charsets.UTF_8));
    assertThat(mapper.writeValueAsString(node.get("dashboard"))).isEqualTo(dashboard);
    assertThat(node.get("overwrite").asBoolean()).isFalse();
    assertThat(node.get("message").asText()).isNotNull();
  }

  @Test
  public void shouldImportDashboard() throws InterruptedException, IOException {
    final var dashboard = "{\"refresh\":false}";
    final var setDashboardResponse = "response";

    grafanaMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(setDashboardResponse)
    );

    final var response = client.importDashboard(dashboard).block();
    assertThat(response).isEqualTo(setDashboardResponse);

    final RecordedRequest commandRequest = grafanaMockWebServer.takeRequest();
    assertThat(commandRequest.getPath()).isEqualTo("/api/dashboards/import");
    final var node = mapper.readTree(commandRequest.getBody().readString(Charsets.UTF_8));
    assertThat(mapper.writeValueAsString(node.get("dashboard"))).isEqualTo(dashboard);
    assertThat(node.get("overwrite").asBoolean()).isTrue();
    assertThat(node.get("folderId").asInt()).isEqualTo(0);
  }


  @Test
  public void shouldDeleteDashboard() throws InterruptedException {
    final var id = "id";
    final var deleteDashboardResponse = "deleteDashboardResponse";

    grafanaMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(deleteDashboardResponse)
    );

    final var response = client.deleteDashboard(id).block();
    assertThat(response).isEqualTo(deleteDashboardResponse);

    final RecordedRequest commandRequest = grafanaMockWebServer.takeRequest();
    assertThat(commandRequest.getPath()).isEqualTo("/api/dashboards/uid/id");
  }

  @Test
  public void shouldInitDashboard() throws IOException {
    final var result = client.initDashboard("testId", "title", 42L, ResourceUtils.getResourceContent("grafana-gatling-dashboard.json"));
    assertThat(result).isEqualTo(ResourceUtils.getResourceContent("grafana-gatling-dashboard-result-init.json"));
  }

  @Test(expected = RuntimeException.class)
  public void shouldInitDashboardFail() {
    client.initDashboard("testId", "title", 42L, "ca va fail !!!");
  }

  @Test(expected = RuntimeException.class)
  public void shouldUpdatedDashboardFail() {
    client.updatedDashboard(42L, "ca va fail !!!");
  }

  @Test
  public void shouldUpdatedDashboardRunning() throws IOException {
    final var result = client.updatedDashboard(42L, ResourceUtils.getResourceContent("grafana-gatling-dashboard.json"));
    assertThat(result).isEqualTo(ResourceUtils.getResourceContent("grafana-gatling-dashboard-result-running.json"));
  }

  @Test
  public void shouldUpdatedDashboardCompleted() throws IOException {
    final var result = client.updatedDashboard(42L, ResourceUtils.getResourceContent("grafana-gatling-dashboard.json"));
    assertThat(result).isEqualTo(ResourceUtils.getResourceContent("grafana-gatling-dashboard-result-completed.json"));
  }

  @Test
  public void shouldUpdatedDashboardRefresh() throws IOException {
    final var result = client.updatedDashboard(0L, ResourceUtils.getResourceContent("grafana-gatling-dashboard.json"));
    assertThat(result).contains("\"refresh\":\"1s\"");
  }
}
