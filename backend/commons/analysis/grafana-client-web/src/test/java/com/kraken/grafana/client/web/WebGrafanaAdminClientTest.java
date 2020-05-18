package com.kraken.grafana.client.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.Application;
import com.kraken.config.grafana.api.GrafanaProperties;
import com.kraken.config.influxdb.api.InfluxDBProperties;
import com.kraken.grafana.client.api.GrafanaAdminClient;
import com.kraken.grafana.client.api.GrafanaUser;
import com.kraken.tools.unique.id.IdGenerator;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.kraken.grafana.client.api.GrafanaUserTest.GRAFANA_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class WebGrafanaAdminClientTest {

  private ObjectMapper mapper;
  private MockWebServer server;
  private GrafanaAdminClient client;

  @MockBean
  GrafanaProperties grafanaProperties;
  @MockBean
  InfluxDBProperties influxDBProperties;
  @MockBean
  IdGenerator idGenerator;

  @Before
  public void before() {
    mapper = new ObjectMapper();
    server = new MockWebServer();
    final String url = server.url("/").toString();
    when(grafanaProperties.getUrl()).thenReturn(url);
    client = new WebGrafanaAdminClient(grafanaProperties, idGenerator, mapper);
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  public void shouldCreateUser() throws InterruptedException, IOException {
    given(idGenerator.generate()).willReturn("password", "datasourceName");

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(CreateGrafanaUserResponseTest.RESPONSE))
    );

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("[{\"orgId\":42,\"name\":\"Some Org.\",\"role\":\"Admin\"}]")
    );

    final var user = client.createUser("userId", "email").block();
    assertThat(user).isEqualTo(GrafanaUser.builder()
        .id("" + CreateGrafanaUserResponseTest.RESPONSE.getId())
        .password("password")
        .email("email")
        .datasourceName("userId")
        .orgId("42")
        .build());

    final RecordedRequest userRequest = server.takeRequest();
    assertThat(userRequest.getPath()).isEqualTo("/api/admin/users");
    assertThat(userRequest.getMethod()).isEqualTo("POST");
    assertThat(userRequest.getBody().readUtf8()).isEqualTo(mapper.writeValueAsString(CreateGrafanaUserRequest.builder()
        .name("email")
        .email("email")
        .login("userId")
        .password("password")
        .build()));

    final RecordedRequest orgRequest = server.takeRequest();
    assertThat(orgRequest.getPath()).isEqualTo("/api/users/2/orgs");
    assertThat(orgRequest.getMethod()).isEqualTo("GET");
  }

  @Test
  public void shouldDeleteUser() throws InterruptedException, IOException {
    given(idGenerator.generate()).willReturn("password", "datasourceName");

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(FindGrafanaUserResponseTest.RESPONSE))
    );
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
    );
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
    );

    assertThat(client.deleteUser("userId").block()).isNotNull();

    final RecordedRequest findUserRequest = server.takeRequest();
    assertThat(findUserRequest.getPath()).isEqualTo("/api/users/lookup?loginOrEmail=userId");
    assertThat(findUserRequest.getMethod()).isEqualTo("GET");

    final RecordedRequest deleteUserRequest = server.takeRequest();
    assertThat(deleteUserRequest.getPath()).isEqualTo("/api/admin/users/" + FindGrafanaUserResponseTest.RESPONSE.getId());
    assertThat(deleteUserRequest.getMethod()).isEqualTo("DELETE");

    final RecordedRequest deleteOrgRequest = server.takeRequest();
    assertThat(deleteOrgRequest.getPath()).isEqualTo("/api/orgs/" + FindGrafanaUserResponseTest.RESPONSE.getOrgId());
    assertThat(deleteOrgRequest.getMethod()).isEqualTo("DELETE");
  }

  @Test
  public void shouldUpdateUser() throws InterruptedException, IOException {
    given(idGenerator.generate()).willReturn("password", "datasourceName");

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setBody("")
    );
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setBody("")
    );

    final var email = "other";
    final var user = client.updateUser(GRAFANA_USER, "userId",  email).block();
    assertThat(user).isEqualTo(
        GRAFANA_USER.toBuilder()
            .email("other")
            .build());

    final RecordedRequest userRequest = server.takeRequest();
    assertThat(userRequest.getPath()).isEqualTo("/api/users/" + GRAFANA_USER.getId());
    assertThat(userRequest.getMethod()).isEqualTo("PUT");
    assertThat(userRequest.getBody().readUtf8()).isEqualTo(mapper.writeValueAsString(UpdateGrafanaUserRequest.builder()
        .name(email)
        .email(email)
        .login("userId")
        .build()));

    final RecordedRequest orgRequest = server.takeRequest();
    assertThat(orgRequest.getPath()).isEqualTo("/api/orgs/" + GRAFANA_USER.getOrgId());
    assertThat(orgRequest.getMethod()).isEqualTo("PUT");
    assertThat(orgRequest.getBody().readUtf8()).isEqualTo(mapper.writeValueAsString(UpdateGrafanaOrganizationRequest.builder()
        .name(email)
        .build()));

  }
}
