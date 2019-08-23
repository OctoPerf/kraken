package com.kraken.influxdb.client;

import com.google.common.base.Charsets;
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

import static org.assertj.core.api.Assertions.assertThat;

public class InfluxDBClientTest {

  private MockWebServer influxDbMockWebServer;
  private InfluxDBClient client;

  @Before
  public void before() {
    influxDbMockWebServer = new MockWebServer();
    client = new InfluxDBWebClient(InfluxDBClientPropertiesTest.INFLUX_DB_CLIENT_PROPERTIES, WebClient.create(influxDbMockWebServer.url("/").toString()));
  }

  @After
  public void tearDown() throws IOException {
    influxDbMockWebServer.shutdown();
  }

  @Test
  public void shouldDeleteSeries() throws InterruptedException {
    final var testId = "testId";

    influxDbMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("{}")
    );

    final var response = client.deleteSeries(testId).block();
    assertThat(response).isEqualTo("{}");

    final RecordedRequest commandRequest = influxDbMockWebServer.takeRequest();
    assertThat(commandRequest.getPath()).isEqualTo("/query?db=influxdbDatabase");
    assertThat(commandRequest.getBody().readString(Charsets.UTF_8)).isEqualTo("q=DROP+SERIES+FROM+%2F.*%2F+WHERE+test+%3D+%27testId%27");
  }
}
