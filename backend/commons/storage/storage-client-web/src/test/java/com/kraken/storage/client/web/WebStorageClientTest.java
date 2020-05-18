package com.kraken.storage.client.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.kraken.Application;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultTest;
import com.kraken.config.storage.client.api.StorageClientProperties;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.security.authentication.api.ExchangeFilterFactory;
import com.kraken.storage.client.api.StorageClient;
import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageWatcherEventTest;
import com.kraken.tools.configuration.jackson.MediaTypes;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import okio.Okio;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.kraken.analysis.entity.ResultType.RUN;
import static com.kraken.storage.entity.StorageNodeTest.STORAGE_NODE;
import static com.kraken.storage.entity.StorageNodeType.DIRECTORY;
import static com.kraken.storage.entity.StorageNodeType.FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes =  Application.class)
public class WebStorageClientTest {

  private MockWebServer server;
  private StorageClient client;

  @Autowired
  List<ExchangeFilterFactory> filterFactories;


  @Autowired
  @Qualifier("yamlObjectMapper")
  ObjectMapper yamlMapper;
  @Autowired
  ObjectMapper jsonMapper;
  @MockBean
  StorageClientProperties properties;

  @Before
  public void before() {
    server = new MockWebServer();
    final String baseUrl = server.url("/").toString();
    when(properties.getUrl()).thenReturn(baseUrl);
    client = new WebStorageClientBuilder(filterFactories, properties, jsonMapper, yamlMapper).mode(AuthenticationMode.NOOP).build();
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  public void shouldCreateFolder() throws InterruptedException, IOException {
    final var directoryNode = StorageNode.builder()
        .depth(0)
        .path("path")
        .type(DIRECTORY)
        .length(0L)
        .lastModified(0L)
        .build();

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(jsonMapper.writeValueAsString(directoryNode))
    );

    final var response = client.createFolder("path").block();
    assertThat(response).isEqualTo(directoryNode);

    final var directoryRequest = server.takeRequest();
    assertThat(directoryRequest.getPath()).startsWith("/files/set/directory?path=path");
  }

  @Test
  public void shouldDelete() throws InterruptedException {
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("[true]")
    );
    final var response = client.delete("path").block();
    assertThat(response).isEqualTo(true);

    final var recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getBody().readString(Charsets.UTF_8)).isEqualTo("[\"path\"]");
  }

  @Test
  public void shouldSetContent() throws InterruptedException, IOException {
    final var fileNode = StorageNode.builder()
        .depth(1)
        .path("path/result.son")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(jsonMapper.writeValueAsString(fileNode))
    );

    final var response = client.setContent("path", "content").block();
    assertThat(response).isEqualTo(fileNode);

    final var recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/set/content?path=path");
    assertThat(recordedRequest.getBody().readString(Charsets.UTF_8)).isEqualTo("content");
  }

  @Test
  public void shouldWatch() throws InterruptedException, JsonProcessingException {
    final var body = ":keep alive\n" +
        "\n" +
        "data:" + jsonMapper.writeValueAsString(StorageWatcherEventTest.STORAGE_WATCHER_EVENT) + "\n" +
        "\n" +
        ":keep alive\n" +
        "\n" +
        "data:" + jsonMapper.writeValueAsString(StorageWatcherEventTest.STORAGE_WATCHER_EVENT) + "\n" +
        "\n" +
        ":keep alive\n" +
        "\n" +
        "data:" + jsonMapper.writeValueAsString(StorageWatcherEventTest.STORAGE_WATCHER_EVENT) + "\n" +
        "\n" +
        "data:" + jsonMapper.writeValueAsString(StorageWatcherEventTest.STORAGE_WATCHER_EVENT) + "\n" +
        "\n";

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
            .setBody(body));

    final var response = client.watch().collectList().block();
    assertThat(response).isNotNull();
    assertThat(response.size()).isEqualTo(4);

    final var request = server.takeRequest();
    assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.TEXT_EVENT_STREAM_VALUE);
    assertThat(request.getPath()).isEqualTo("/files/watch");
  }

  @Test
  public void shouldGetContent() throws InterruptedException {
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .setBody("content")
    );

    final var response = client.getContent("path").block();
    assertThat(response).isEqualTo("content");

    final var recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/get/content?path=path");
  }

  @Test
  public void shouldSetJsonContent() throws InterruptedException, IOException {
    final var fileNode = StorageNode.builder()
        .depth(1)
        .path("path/result.son")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();
    final var result = ResultTest.RESULT;

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(jsonMapper.writeValueAsString(fileNode))
    );

    final var response = client.setJsonContent("path", result).block();
    assertThat(response).isEqualTo(fileNode);

    final var recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/set/content?path=path");
    assertThat(recordedRequest.getBody().readString(Charsets.UTF_8)).isEqualTo(jsonMapper.writeValueAsString(result));
  }

  @Test
  public void shouldGetJsonContent() throws InterruptedException, IOException {
    final var result = Result.builder()
        .id("id")
        .description("description")
        .status(ResultStatus.STARTING)
        .endDate(0L)
        .startDate(42L)
        .type(RUN)
        .build();

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(jsonMapper.writeValueAsString(result))
    );

    final var response = client.getJsonContent("path", Result.class).block();
    assertThat(response).isEqualTo(result);

    final var recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/get/json?path=path");
  }

  @Test
  public void shouldGetYamlContent() throws InterruptedException, IOException {
    final var result = ResultTest.RESULT;

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaTypes.TEXT_YAML_VALUE)
            .setBody(yamlMapper.writeValueAsString(result))
    );

    final var response = client.getYamlContent("path", Result.class).block();
    assertThat(response).isEqualTo(result);

    final var recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/get/content?path=path");
  }

  @Test
  public void shouldDownloadFile() throws InterruptedException, IOException {
    final var testFile = Paths.get("testDir/test.xml");
    final var outFile = Paths.get("testDir/out.xml");

    final var buffer = new Buffer();
    buffer.writeAll(Okio.source(testFile.toFile()));

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
            .setChunkedBody(buffer, 128)
    );

    client.downloadFile(outFile, testFile.toString()).block();

    try {
      final var test = Files.readString(testFile, Charsets.UTF_8);
      final var out = Files.readString(outFile, Charsets.UTF_8);
      assertThat(test).isEqualTo(out);
      System.out.println(out);
      Files.delete(outFile);
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }

    final RecordedRequest recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/get/file?path=testDir/test.xml");
  }

  @Test
  public void shouldDownloadFolder() throws InterruptedException, IOException {
    final var testFile = "testDir/gatling.zip";
    final var outDir = Paths.get("testDir/gatling");

    assertThat(outDir.toFile().mkdirs()).isTrue();

    final var buffer = new Buffer();
    buffer.writeAll(Okio.source(new File(testFile)));

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
            .setChunkedBody(buffer, 128)
    );


    client.downloadFolder(outDir, "").block();

    final RecordedRequest recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/get/file?path=");

    assertThat(Files.list(outDir).count()).isEqualTo(7);
    FileSystemUtils.deleteRecursively(outDir);
  }

  @Test
  public void shouldUploadFile() throws InterruptedException, IOException {
    final var testFile = Paths.get("testDir/test.xml");

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(jsonMapper.writeValueAsString(STORAGE_NODE))
    );

    final var response = client.uploadFile(testFile, "").block();
    assertThat(response).isEqualTo(STORAGE_NODE);

    final RecordedRequest recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/set/zip?path=");
    // Must be compressed !
    assertThat(recordedRequest.getBodySize()).isLessThan(testFile.toFile().length());
  }

  @Test
  public void shouldUploadFolder() throws InterruptedException, IOException {
    final var testFile = Paths.get("testDir/zipDir");

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(jsonMapper.writeValueAsString(STORAGE_NODE))
    );

    final var response = client.uploadFile(testFile, "path").block();
    assertThat(response).isEqualTo(STORAGE_NODE);

    final RecordedRequest setRequest = server.takeRequest();
    assertThat(setRequest.getPath()).isEqualTo("/files/set/zip?path=path");
  }
}
