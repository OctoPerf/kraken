package com.kraken.analysis.client.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.kraken.Application;
import com.kraken.analysis.client.api.AnalysisClient;
import com.kraken.analysis.entity.DebugEntryTest;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultTest;
import com.kraken.config.analysis.client.api.AnalysisClientProperties;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.security.authentication.api.ExchangeFilterFactory;
import com.kraken.storage.entity.StorageNodeTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class WebAnalysisClientTest {

  private ObjectMapper mapper;
  private MockWebServer server;
  private AnalysisClient client;

  @Autowired
  List<ExchangeFilterFactory> filterFactories;
  @MockBean
  AnalysisClientProperties properties;

  @Before
  public void before() {
    mapper = new ObjectMapper();
    server = new MockWebServer();

    final String url = server.url("/").toString();
    when(properties.getUrl()).thenReturn(url);
    client = new WebAnalysisClientBuilder(filterFactories, properties).mode(AuthenticationMode.NOOP).build();
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  public void shouldCreateResult() throws InterruptedException, JsonProcessingException {
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(StorageNodeTest.STORAGE_NODE))
    );

    final var response = client.create(ResultTest.RESULT).block();
    assertThat(response).isEqualTo(StorageNodeTest.STORAGE_NODE);

    final RecordedRequest request = server.takeRequest();
    assertThat(request.getPath()).isEqualTo("/result");
    assertThat(request.getBody().readString(Charsets.UTF_8)).isEqualTo(mapper.writeValueAsString(ResultTest.RESULT));
  }

  @Test
  public void shouldDeleteResult() throws InterruptedException {
    final var resultId = "resultId";

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .setBody(resultId)
    );

    final var response = client.delete(resultId).block();
    assertThat(response).isEqualTo(resultId);

    final RecordedRequest request = server.takeRequest();
    assertThat(request.getPath()).isEqualTo("/result?resultId=resultId");
    assertThat(request.getMethod()).isEqualTo("DELETE");
  }

  @Test
  public void shouldSetStatus() throws InterruptedException, JsonProcessingException {
    final var resultId = "resultId";
    final var status = ResultStatus.COMPLETED;

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(StorageNodeTest.STORAGE_NODE))
    );

    final var response = client.setStatus(resultId, status).block();
    assertThat(response).isEqualTo(StorageNodeTest.STORAGE_NODE);

    final RecordedRequest request = server.takeRequest();
    assertThat(request.getPath()).isEqualTo("/result/status/COMPLETED?resultId=resultId");
    assertThat(request.getMethod()).isEqualTo("POST");
    assertThat(request.getBody().readString(Charsets.UTF_8)).isEqualTo("");
  }

  @Test
  public void shouldAddDebugEntry() throws InterruptedException, JsonProcessingException {
    final var debug = DebugEntryTest.DEBUG_ENTRY;

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(debug))
    );

    final var response = client.debug(debug).block();
    assertThat(response).isEqualTo(debug);

    final RecordedRequest request = server.takeRequest();
    assertThat(request.getPath()).isEqualTo("/result/debug");
    assertThat(request.getMethod()).isEqualTo("POST");
    assertThat(request.getBody().readString(Charsets.UTF_8)).isEqualTo(mapper.writeValueAsString(debug));
  }
}
