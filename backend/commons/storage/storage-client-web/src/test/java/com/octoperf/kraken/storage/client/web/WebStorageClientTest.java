package com.octoperf.kraken.storage.client.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.Application;
import com.octoperf.kraken.analysis.entity.Result;
import com.octoperf.kraken.analysis.entity.ResultStatus;
import com.octoperf.kraken.analysis.entity.ResultTest;
import com.octoperf.kraken.config.backend.client.api.BackendClientProperties;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.api.ExchangeFilterFactory;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.entity.StorageNode;
import com.octoperf.kraken.storage.entity.StorageWatcherEvent;
import com.octoperf.kraken.storage.entity.StorageWatcherEventTest;
import com.octoperf.kraken.tools.configuration.jackson.MediaTypes;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import okio.Okio;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.octoperf.kraken.analysis.entity.ResultType.RUN;
import static com.octoperf.kraken.storage.entity.StorageNodeType.DIRECTORY;
import static com.octoperf.kraken.storage.entity.StorageNodeType.FILE;
import static com.octoperf.kraken.storage.entity.StorageWatcherEventType.CREATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
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
  BackendClientProperties properties;

  @BeforeEach
  public void before() {
    server = new MockWebServer();
    final String baseUrl = server.url("/").toString();
    when(properties.getUrl()).thenReturn(baseUrl);
    client = new WebStorageClientBuilder(filterFactories, properties, jsonMapper, yamlMapper).build(AuthenticatedClientBuildOrder.NOOP).block();
  }

  @AfterEach
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
            .setBody(jsonMapper.writeValueAsString(ImmutableList.of(StorageWatcherEvent.builder()
                .node(directoryNode)
                .type(CREATE)
                .owner(Owner.PUBLIC)
                .build())))
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
            .setBody("[]")
    );
    final var response = client.delete("path").block();
    assertThat(response).isEqualTo(false);

    final var recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getBody().readString(Charsets.UTF_8)).isEqualTo("[\"path\"]");
  }

  @Test
  public void shouldDeleteTrue() throws InterruptedException, JsonProcessingException {
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(jsonMapper.writeValueAsString(ImmutableList.of(StorageWatcherEventTest.STORAGE_WATCHER_EVENT)))
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
            .setBody(jsonMapper.writeValueAsString(ImmutableList.of(StorageWatcherEvent.builder().node(fileNode).owner(Owner.PUBLIC).type(CREATE).build())))
    );

    final var response = client.setContent(fileNode.getPath(), "content").block();
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
            .setBody(jsonMapper.writeValueAsString(ImmutableList.of(StorageWatcherEvent.builder().node(fileNode).owner(Owner.PUBLIC).type(CREATE).build())))
    );

    final var response = client.setJsonContent(fileNode.getPath(), result).block();
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
    final var fileNode = StorageNode.builder()
        .depth(1)
        .path("remote")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();
    final var events = ImmutableList.of(StorageWatcherEvent.builder().node(fileNode).owner(Owner.PUBLIC).type(CREATE).build());

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(jsonMapper.writeValueAsString(events))
    );

    final var response = client.uploadFile(testFile, fileNode.getPath()).collectList().block();
    assertThat(response).isEqualTo(events);

    final RecordedRequest recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/set/zip?path=");
    // Must be compressed !
    assertThat(recordedRequest.getBodySize()).isLessThan(testFile.toFile().length());
  }

  @Test
  public void shouldUploadFolder() throws InterruptedException, IOException {
    final var testFile = Paths.get("testDir/zipDir");
    final var directoryNode = StorageNode.builder()
        .depth(1)
        .path("remote")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();
    final var events = ImmutableList.of(StorageWatcherEvent.builder().node(directoryNode).owner(Owner.PUBLIC).type(CREATE).build());

    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(jsonMapper.writeValueAsString(events))
    );

    final var response = client.uploadFile(testFile, directoryNode.getPath()).collectList().block();
    assertThat(response).isEqualTo(events);

    final RecordedRequest setRequest = server.takeRequest();
    assertThat(setRequest.getPath()).isEqualTo("/files/set/zip?path=remote");
  }
}
