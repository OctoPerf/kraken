package com.octoperf.kraken.grafana.client.web;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.influxdb.api.InfluxDBProperties;
import com.octoperf.kraken.grafana.client.api.GrafanaAdminClient;
import com.octoperf.kraken.grafana.client.api.GrafanaUser;
import com.octoperf.kraken.grafana.client.api.GrafanaUserClient;
import com.octoperf.kraken.grafana.client.api.GrafanaUserClientBuilder;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUser;
import com.octoperf.kraken.tests.utils.ResourceUtils;
import com.octoperf.kraken.tools.unique.id.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

// Start grafana before running
@Tag("integration")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Application.class})
@SpringBootTest
public class WebGrafanaClientIntegrationTest {

  @Autowired
  GrafanaAdminClient grafanaAdminClient;

  @Autowired
  WebGrafanaUserClientBuilder grafanaUserClientBuilder;

  @MockBean
  IdGenerator idGenerator;
  @MockBean
  InfluxDBProperties dbProperties;

  InfluxDBUser dbUser;
  GrafanaUser grafanaUser;
  Mono<GrafanaUserClient> grafanaUserClient;

  @BeforeEach
  public void setUp() {
    given(dbProperties.getUrl()).willReturn("http://localhost:8086");
    given(idGenerator.generate()).willReturn("password", "datasourceName");


    dbUser = InfluxDBUser.builder()
        .username("kraken_559fe423_214e_4061_8651_dfc24b86c057")
        .password("yc1hl8gir0")
        .database("kraken_559fe423_214e_4061_8651_dfc24b86c057")
        .build();
    grafanaUser = GrafanaUser.builder()
        .datasourceName("559fe423-214e-4061-8651-dfc24b86c057")
        .email("test@test")
        .password("4rnqefazz9")
        .id("2")
        .orgId("2")
        .build();

    grafanaUserClient = grafanaUserClientBuilder.build(grafanaUser, dbUser);
  }

  @Test
  public void shouldCreateFolder() {
    final var id = grafanaUserClient.flatMap(client -> client.createFolder("someNewId", "TheTitle")).block();
    assertThat(id).isNotNull();
    System.out.println(id);
  }

  @Test
  public void shouldGetFolderAndImportDashboard() throws IOException {
    final var dashboard = ResourceUtils.getResourceContent("grafana-gatling-dashboard-integration.json");
    final var imported = grafanaUserClient.flatMap(client -> client.getFolderId("someNewId")
        .flatMap(id -> client.importDashboard("guluilscfl", id, "Title", Instant.now().toEpochMilli(), dashboard))).block();
    assertThat(imported).isNotNull();
    System.out.println(imported);
  }

  @Test
  public void shouldDeleteFolder() {
    final var result = grafanaUserClient.flatMap(client -> client.deleteFolder("someNewId")).block();
    assertThat(result).isNotNull();
  }

  @Test
  public void shouldImportDashboard() throws IOException {
    final var dashboard = ResourceUtils.getResourceContent("grafana-gatling-dashboard-integration.json");
    final var imported = grafanaUserClient.flatMap(client -> client.importDashboard("c8m9z9pkdq", 2L, "Title", Instant.now().toEpochMilli(), dashboard)).block();
    assertThat(imported).isNotNull();
    System.out.println(imported);
  }

  @Test
  public void shouldCreateUser() {
    final var user = grafanaAdminClient.createUser("userId", "test1@octoperf.com").block();
    assertThat(user).isNotNull();
    System.out.println(user);
  }

  @Test
  public void shouldDeleteDashboard() {
    final var result = grafanaUserClient.flatMap(client -> client.deleteDashboard("wghgcsbyz1")).block();
    assertThat(result).isNotNull();
  }

  @Test
  public void shouldCreateDatasource() {
    final var result = grafanaUserClient.flatMap(GrafanaUserClient::createDatasource).block();
    assertThat(result).isNotNull();
  }


  @Test
  public void shouldDeleteUser() {
    final var result = grafanaAdminClient.deleteUser("e9a8b7ec-5b94-4155-8ca3-77c754d31322").block();
    assertThat(result).isNotNull();
  }
}
