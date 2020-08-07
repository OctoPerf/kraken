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
  GrafanaUserClientBuilder grafanaUserClientBuilder;

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

//    Updated user attributes {databaseUsername=[kraken_eb274740_db1e_4c6c_8050_3f0e3073d487], databasePassword=[cfhzmjgopm], databaseName=[kraken_eb274740_db1e_4c6c_8050_3f0e3073d487], dashboardUserId=[2], dashboardEmail=[kojiro.sazaki@gmail.com], dashboardPassword=[rkapa5sfs1], dashboardDatasourceName=[eb274740-db1e-4c6c-8050-3f0e3073d487], dashboardOrgId=[2]}

    dbUser = InfluxDBUser.builder()
        .username("kraken_33368a11_9f9a_4e7f_9c36_2e5c6cd65090")
        .password("lvfolkvwsi")
        .database("kraken_33368a11_9f9a_4e7f_9c36_2e5c6cd65090")
        .build();
    grafanaUser = GrafanaUser.builder()
        .datasourceName("33368a11-9f9a-4e7f-9c36-2e5c6cd65090")
        .email("test@test")
        .password("ef28sbznhp")
        .id("2")
        .orgId("2")
        .build();

    grafanaUserClient = grafanaUserClientBuilder.grafanaUser(grafanaUser).influxDBUser(dbUser).build();
  }

  @Test
  public void shouldImportDashboard() throws IOException {
    final var dashboard = ResourceUtils.getResourceContent("grafana-gatling-dashboard-integration.json");
    final var imported = grafanaUserClient.flatMap(client -> client.importDashboard("c8m9z9pkdq", "Title", Instant.now().toEpochMilli(), dashboard)).block();
    System.out.println(imported);
  }

  @Test
  public void shouldCreateUser() {
    final var user = grafanaAdminClient.createUser("userId", "test1@octoperf.com").block();
    System.out.println(user);
  }

  @Test
  public void shouldDeleteDashboard() {
    grafanaUserClient.flatMap(client -> client.deleteDashboard("wghgcsbyz1")).block();
  }

  @Test
  public void shouldCreateDatasource() {
    grafanaUserClient.flatMap(GrafanaUserClient::createDatasource).block();
  }


  @Test
  public void shouldDeleteUser() {
    grafanaAdminClient.deleteUser("e9a8b7ec-5b94-4155-8ca3-77c754d31322").block();
  }
}
