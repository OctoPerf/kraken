package com.kraken.analysis.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.kraken.analysis.entity.DebugEntryTest;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultTest;
import com.kraken.storage.entity.StorageNodeTest;
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

public class AnalysisClientTest {

  private ObjectMapper mapper;
  private MockWebServer analysisMockWebServer;
  private AnalysisClient client;

  @Before
  public void before() {
    mapper = new ObjectMapper();
    analysisMockWebServer = new MockWebServer();
    client = new AnalysisWebClient(AnalysisClientPropertiesTest.ANALYSIS_CLIENT_PROPERTIES, WebClient.create(analysisMockWebServer.url("/").toString()));
  }

  @After
  public void tearDown() throws IOException {
    analysisMockWebServer.shutdown();
  }

  @Test
  public void shouldCreateResult() throws InterruptedException, JsonProcessingException {
    analysisMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(StorageNodeTest.STORAGE_NODE))
    );

    final var response = client.create(ResultTest.RESULT).block();
    assertThat(response).isEqualTo(StorageNodeTest.STORAGE_NODE);

    final RecordedRequest request = analysisMockWebServer.takeRequest();
    assertThat(request.getPath()).isEqualTo("/result");
    assertThat(request.getBody().readString(Charsets.UTF_8)).isEqualTo(mapper.writeValueAsString(ResultTest.RESULT));
  }

  @Test
  public void shouldDeleteResult() throws InterruptedException {
    final var resultId = "resultId";

    analysisMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .setBody(resultId)
    );

    final var response = client.delete(resultId).block();
    assertThat(response).isEqualTo(resultId);

    final RecordedRequest request = analysisMockWebServer.takeRequest();
    assertThat(request.getPath()).isEqualTo("/result?resultId=resultId");
    assertThat(request.getMethod()).isEqualTo("DELETE");
  }

  @Test
  public void shouldSetStatus() throws InterruptedException, JsonProcessingException {
    final var resultId = "resultId";
    final var status = ResultStatus.COMPLETED;

    analysisMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(StorageNodeTest.STORAGE_NODE))
    );

    final var response = client.setStatus(resultId, status).block();
    assertThat(response).isEqualTo(StorageNodeTest.STORAGE_NODE);

    final RecordedRequest request = analysisMockWebServer.takeRequest();
    assertThat(request.getPath()).isEqualTo("/result/status/COMPLETED?resultId=resultId");
    assertThat(request.getMethod()).isEqualTo("POST");
    assertThat(request.getBody().readString(Charsets.UTF_8)).isEqualTo("");
  }

  @Test
  public void shouldAddDebugEntry() throws InterruptedException, JsonProcessingException {
    final var debug = DebugEntryTest.DEBUG_ENTRY;

    analysisMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(debug))
    );

    final var response = client.debug(debug).block();
    assertThat(response).isEqualTo(debug);

    final RecordedRequest request = analysisMockWebServer.takeRequest();
    assertThat(request.getPath()).isEqualTo("/result/debug");
    assertThat(request.getMethod()).isEqualTo("POST");
    assertThat(request.getBody().readString(Charsets.UTF_8)).isEqualTo(mapper.writeValueAsString(debug));
  }
}
