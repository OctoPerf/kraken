package com.kraken.influxdb.client.web;

import com.google.common.base.Charsets;
import com.kraken.config.influxdb.api.InfluxDBProperties;
import com.kraken.influxdb.client.api.InfluxDBClient;
import com.kraken.influxdb.client.api.InfluxDBUser;
import com.kraken.influxdb.client.api.InfluxDBUserTest;
import com.kraken.tools.unique.id.IdGenerator;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class WebInfluxDBClientTest {

  private MockWebServer server;
  private InfluxDBClient client;

  @Mock
  InfluxDBProperties properties;

  @Mock
  IdGenerator idGenerator;

  @Before
  public void before() {
    server = new MockWebServer();
    when(properties.getUrl()).thenReturn(server.url("/").toString());
    when(properties.getUser()).thenReturn("root");
    when(properties.getPassword()).thenReturn("admin");
    client = new WebInfluxDBClient(properties, idGenerator);
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  public void shouldDeleteSeries() throws InterruptedException {
    final var testId = "testId";
    final var database = "influxdbDatabase";

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("{}")
    );

    final var response = client.deleteSeries(database, testId).block();
    assertThat(response).isEqualTo("{}");

    final RecordedRequest commandRequest = server.takeRequest();
    assertThat(commandRequest.getPath()).isEqualTo("/query?db=influxdbDatabase");
    assertThat(commandRequest.getBody().readString(Charsets.UTF_8)).isEqualTo("q=DROP+SERIES+FROM+%2F.*%2F+WHERE+test+%3D+%27testId%27");
  }

  @Test
  public void shouldCreateUser() throws InterruptedException {
    BDDMockito.given(idGenerator.generate()).willReturn("pwd");

    server.enqueue(new MockResponse().setResponseCode(200));
    server.enqueue(new MockResponse().setResponseCode(200));
    server.enqueue(new MockResponse().setResponseCode(200));

    final var user = client.createUser("user-id").block();
    assertThat(user).isEqualTo(InfluxDBUser.builder()
        .username("kraken_user_id")
        .password("pwd")
        .database("kraken_user_id")
        .build());

    final RecordedRequest createUserRequest = server.takeRequest();
    assertThat(createUserRequest.getPath()).isEqualTo("/query");
    assertThat(createUserRequest.getBody().readString(Charsets.UTF_8)).isEqualTo("q=CREATE+USER+kraken_user_id+WITH+PASSWORD+%27pwd%27");

    final RecordedRequest createDBRequest = server.takeRequest();
    assertThat(createDBRequest.getPath()).isEqualTo("/query");
    assertThat(createDBRequest.getBody().readString(Charsets.UTF_8)).isEqualTo("q=CREATE+DATABASE+kraken_user_id");

    final RecordedRequest grantRequest = server.takeRequest();
    assertThat(grantRequest.getPath()).isEqualTo("/query");
    assertThat(grantRequest.getBody().readString(Charsets.UTF_8)).isEqualTo("q=GRANT+ALL+ON+kraken_user_id+TO+kraken_user_id");
  }

  @Test
  public void shouldDeleteUser() throws InterruptedException {

    server.enqueue(new MockResponse().setResponseCode(200));
    server.enqueue(new MockResponse().setResponseCode(200));

    assertThat(client.deleteUser("user-id").block()).isNotNull();

    final RecordedRequest createUserRequest = server.takeRequest();
    assertThat(createUserRequest.getPath()).isEqualTo("/query");
    assertThat(createUserRequest.getBody().readString(Charsets.UTF_8)).isEqualTo("q=DROP+DATABASE+kraken_user_id");

    final RecordedRequest createDBRequest = server.takeRequest();
    assertThat(createDBRequest.getPath()).isEqualTo("/query");
    assertThat(createDBRequest.getBody().readString(Charsets.UTF_8)).isEqualTo("q=DROP+USER+kraken_user_id");
  }
}
