package com.octoperf.kraken.grafana.client.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.grafana.api.GrafanaProperties;
import com.octoperf.kraken.config.influxdb.api.InfluxDBProperties;
import com.octoperf.kraken.grafana.client.api.GrafanaUser;
import com.octoperf.kraken.grafana.client.api.GrafanaUserClient;
import com.octoperf.kraken.grafana.client.api.GrafanaUserTest;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUser;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUserTest;
import com.octoperf.kraken.tests.utils.ResourceUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class WebGrafanaUserClientTest {

  private ObjectMapper mapper;
  private MockWebServer server;
  private GrafanaUserClient client;
  private GrafanaUser grafanaUser;
  private InfluxDBUser influxDBUser;

  @Autowired
  WebGrafanaUserClientBuilder clientBuilder;

  @MockBean
  GrafanaProperties grafanaProperties;
  @MockBean
  InfluxDBProperties influxDBProperties;

  @BeforeEach
  public void before() throws InterruptedException {
    grafanaUser = GrafanaUserTest.GRAFANA_USER;
    influxDBUser = InfluxDBUserTest.INFLUX_DB_USER;

    mapper = new ObjectMapper();
    server = new MockWebServer();
    final String url = server.url("/").toString();
    when(grafanaProperties.getUrl()).thenReturn(url);

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.SET_COOKIE, WebGrafanaUserClientBuilder.GRAFANA_SESSION + "=sessionId")
    );
    client = clientBuilder
        .grafanaUser(grafanaUser)
        .influxDBUser(influxDBUser)
        .build().block();
    final RecordedRequest loginRequest = server.takeRequest();
    assertThat(loginRequest.getPath()).isEqualTo("/login");
    assertThat(loginRequest.getBody().readUtf8()).isEqualTo("{\"user\":\"email\",\"password\":\"password\",\"email\":\"email\"}");
  }

  @AfterEach
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  public void shouldCreateDatasource() throws InterruptedException {
    given(influxDBProperties.getPublishedUrl()).willReturn("url");

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("{\"datasource\":{\"id\":7,\"orgId\":4,\"name\":\"InfluxDB-1\",\"type\":\"influxdb\",\"typeLogoUrl\":\"\",\"access\":\"proxy\",\"url\":\"\",\"password\":\"\",\"user\":\"\",\"database\":\"\",\"basicAuth\":false,\"basicAuthUser\":\"\",\"basicAuthPassword\":\"\",\"withCredentials\":false,\"isDefault\":false,\"jsonData\":{},\"secureJsonFields\":{},\"version\":1,\"readOnly\":false},\"id\":7,\"message\":\"Datasource added\",\"name\":\"InfluxDB-1\"}")
    );

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("")
    );

    final var id = client.createDatasource().block();
    assertThat(id).isNotNull();
    assertThat(id).isEqualTo(7);

    final RecordedRequest createRequest = server.takeRequest();
    assertThat(createRequest.getPath()).isEqualTo("/api/datasources");
    assertThat(createRequest.getMethod()).isEqualTo("POST");
    assertThat(createRequest.getBody().readUtf8()).isEqualTo("{\"name\":\"datasourceName\",\"type\":\"influxdb\",\"access\":\"proxy\",\"isDefault\":true}");
    assertThat(createRequest.getHeader(HttpHeaders.COOKIE)).isEqualTo("grafana_session=sessionId");

//    final RecordedRequest updateRequest = server.takeRequest();
//    assertThat(updateRequest.getPath()).isEqualTo("/api/datasources/7");
//    assertThat(updateRequest.getMethod()).isEqualTo("PUT");
//    assertThat(updateRequest.getBody().readUtf8()).isEqualTo("{\"id\":7,\"orgId\":4,\"name\":\"InfluxDB-1\",\"type\":\"influxdb\",\"typeLogoUrl\":\"\",\"access\":\"proxy\",\"url\":\"url\",\"password\":\"\",\"user\":\"username\",\"database\":\"database\",\"basicAuth\":false,\"basicAuthUser\":\"\",\"basicAuthPassword\":\"\",\"withCredentials\":false,\"isDefault\":false,\"jsonData\":{\"httpMode\":\"POST\"},\"secureJsonFields\":{},\"version\":1,\"readOnly\":false,\"secureJsonData\":{\"password\":\"password\"}}");
//    assertThat(updateRequest.getHeader(HttpHeaders.COOKIE)).isEqualTo("grafana_session=sessionId");
  }

  @Test
  public void shouldDeleteDashboard() throws InterruptedException {
    final var id = "id";
    final var deleteDashboardResponse = "deleteDashboardResponse";

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(deleteDashboardResponse)
    );

    final var response = client.deleteDashboard(id).block();
    assertThat(response).isEqualTo(deleteDashboardResponse);

    final RecordedRequest request = server.takeRequest();
    assertThat(request.getPath()).isEqualTo("/api/dashboards/uid/id");
    assertThat(request.getHeader(HttpHeaders.COOKIE)).isEqualTo("grafana_session=sessionId");
  }

  @Test
  public void shouldImportDashboard() throws InterruptedException, IOException {
    final var dashboard = ResourceUtils.getResourceContent("grafana-gatling-dashboard.json");
    final var initialized = ResourceUtils.getResourceContent("grafana-gatling-dashboard-result-init.json");
    final var setDashboardResponse = "response";

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(setDashboardResponse)
    );

    final var response = client.importDashboard("testId", "title", 42L, dashboard).block();
    assertThat(response).isEqualTo(setDashboardResponse);

    final RecordedRequest request = server.takeRequest();
    assertThat(request.getPath()).isEqualTo("/api/dashboards/import");
    assertThat(request.getHeader(HttpHeaders.COOKIE)).isEqualTo("grafana_session=sessionId");
    final var node = mapper.readTree(request.getBody().readString(Charsets.UTF_8));
    assertThat(mapper.writeValueAsString(node.get("dashboard"))).isEqualTo(initialized);
    assertThat(node.get("overwrite").asBoolean()).isTrue();
    assertThat(node.get("folderId").asInt()).isEqualTo(0);

  }

  @Test
  public void shouldUpdatedDashboardDone() throws IOException, InterruptedException {
    this.shouldUpdate(ResourceUtils.getResourceContent("grafana-gatling-dashboard-result-init.json"),
        ResourceUtils.getResourceContent("grafana-gatling-dashboard-result-done.json"),
        42L);

  }

  @Test
  public void shouldUpdatedDashboardRunning() throws IOException, InterruptedException {
    this.shouldUpdate(ResourceUtils.getResourceContent("grafana-gatling-dashboard-result-init.json"),
        ResourceUtils.getResourceContent("grafana-gatling-dashboard-result-running.json"),
        0L);
  }

  private void shouldUpdate(final String dashboard, final String updated, final Long endDate) throws IOException, InterruptedException {
    final var id = "id";
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("{\"dashboard\":" + dashboard + "}")
    );
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("response")
    );

    final var response = client.updateDashboard(id, endDate).block();
    assertThat(response).isEqualTo(response);

    final RecordedRequest getRequest = server.takeRequest();
    assertThat(getRequest.getPath()).isEqualTo("/api/dashboards/uid/" + id);
    assertThat(getRequest.getHeader(HttpHeaders.COOKIE)).isEqualTo("grafana_session=sessionId");

    final RecordedRequest setRequest = server.takeRequest();
    assertThat(setRequest.getPath()).isEqualTo("/api/dashboards/db");
    final var node = mapper.readTree(setRequest.getBody().readString(Charsets.UTF_8));
    assertThat(mapper.writeValueAsString(node.get("dashboard"))).isEqualTo(updated);
    assertThat(node.get("overwrite").asBoolean()).isFalse();
    assertThat(node.get("message").asText()).isNotNull();
    assertThat(getRequest.getHeader(HttpHeaders.COOKIE)).isEqualTo("grafana_session=sessionId");
  }
}
