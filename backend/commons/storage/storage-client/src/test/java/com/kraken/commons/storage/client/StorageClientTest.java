package com.kraken.commons.storage.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.kraken.commons.analysis.entity.Result;
import com.kraken.commons.analysis.entity.ResultStatus;
import com.kraken.commons.storage.entity.StorageNode;
import com.kraken.commons.storage.entity.StorageNodeTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import okio.Okio;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static com.kraken.commons.analysis.entity.ResultType.RUN;
import static com.kraken.commons.storage.entity.StorageNodeTest.STORAGE_NODE;
import static com.kraken.commons.storage.entity.StorageNodeType.DIRECTORY;
import static com.kraken.commons.storage.entity.StorageNodeType.FILE;
import static org.assertj.core.api.Assertions.assertThat;

public class StorageClientTest {

  private ObjectMapper mapper;
  private MockWebServer storageMockWebServer;
  private StorageClient client;

  @Before
  public void before() {
    storageMockWebServer = new MockWebServer();
    mapper = new ObjectMapper();
    client = new StorageWebClient(WebClient.create(storageMockWebServer.url("/").toString()), mapper);
  }

  @After
  public void tearDown() throws IOException {
    storageMockWebServer.shutdown();
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

    storageMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(directoryNode))
    );

    final var response = client.createFolder("path").block();
    assertThat(response).isEqualTo(directoryNode);

    final var directoryRequest = storageMockWebServer.takeRequest();
    assertThat(directoryRequest.getPath()).startsWith("/files/set/directory?path=path");
  }

  @Test
  public void shouldDelete() throws InterruptedException {
    storageMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("[true]")
    );
    final var response = client.delete("path").block();
    assertThat(response).isEqualTo(true);

    final var recordedRequest = storageMockWebServer.takeRequest();
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

    storageMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(fileNode))
    );

    final var response = client.setContent("path", "content").block();
    assertThat(response).isEqualTo(fileNode);

    final var recordedRequest = storageMockWebServer.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/set/content?path=path");
    assertThat(recordedRequest.getBody().readString(Charsets.UTF_8)).isEqualTo("content");
  }

  @Test
  public void shouldGetContent() throws InterruptedException {
    storageMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("content")
    );

    final var response = client.getContent("path").block();
    assertThat(response).isEqualTo("content");

    final var recordedRequest = storageMockWebServer.takeRequest();
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
    final var result = Result.builder()
        .id("id")
        .runDescription("runDescription")
        .status(ResultStatus.STARTING)
        .endDate(0L)
        .startDate(42L)
        .type(RUN)
        .build();

    storageMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(fileNode))
    );

    final var response = client.setJsonContent("path", result).block();
    assertThat(response).isEqualTo(fileNode);

    final var recordedRequest = storageMockWebServer.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/set/content?path=path");
    assertThat(recordedRequest.getBody().readString(Charsets.UTF_8)).isEqualTo(mapper.writeValueAsString(result));
  }

  @Test
  public void shouldGetJsonContent() throws InterruptedException, IOException {
    final var result = Result.builder()
        .id("id")
        .runDescription("runDescription")
        .status(ResultStatus.STARTING)
        .endDate(0L)
        .startDate(42L)
        .type(RUN)
        .build();

    storageMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(result))
    );

    final var response = client.getJsonContent("path", Result.class).block();
    assertThat(response).isEqualTo(result);

    final var recordedRequest = storageMockWebServer.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/get/json?path=path");
  }

  @Test
  public void shouldDownloadFile() throws InterruptedException, IOException {
    final var testFile = Paths.get("testDir/test.xml");
    final var outFile = Paths.get("testDir/out.xml");

    final var buffer = new Buffer();
    buffer.writeAll(Okio.source(testFile.toFile()));

    storageMockWebServer.enqueue(
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

    final RecordedRequest recordedRequest = storageMockWebServer.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/get/file?path=testDir/test.xml");
  }

  @Test
  public void shouldDownloadFolder() throws InterruptedException, IOException {
    final var testFile = "testDir/gatling.zip";
    final var outDir = Paths.get("testDir/gatling");

    assertThat(outDir.toFile().mkdirs()).isTrue();

    final var buffer = new Buffer();
    buffer.writeAll(Okio.source(new File(testFile)));

    storageMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
            .setChunkedBody(buffer, 128)
    );


    client.downloadFolder(outDir, Optional.empty()).block();

    final RecordedRequest recordedRequest = storageMockWebServer.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/get/file?path=");

    assertThat(Files.list(outDir).count()).isEqualTo(7);
    FileSystemUtils.deleteRecursively(outDir);
  }

  @Test
  public void shouldUploadFile() throws InterruptedException, IOException {
    final var testFile = Paths.get("testDir/test.xml");

    storageMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(STORAGE_NODE))
    );

    final var response = client.uploadFile(testFile, Optional.empty()).block();

    assertThat(response).isEqualTo(STORAGE_NODE);

    final RecordedRequest recordedRequest = storageMockWebServer.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/set/file?path=");
    assertThat(recordedRequest.getBodySize()).isGreaterThan(testFile.toFile().length());
  }

  @Test
  public void shouldUploadFolder() throws InterruptedException, IOException {
    final var testFile = Paths.get("testDir/zipDir");

    storageMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(STORAGE_NODE))
    );

    storageMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(mapper.writeValueAsString(STORAGE_NODE))
    );

    storageMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("[true]")
    );

    final var response = client.uploadFolder(testFile, Optional.empty()).block();

    assertThat(response).isTrue();

    final RecordedRequest setRequest = storageMockWebServer.takeRequest();
    assertThat(setRequest.getPath()).isEqualTo("/files/set/file?path=");

    final RecordedRequest extractRequest = storageMockWebServer.takeRequest();
    assertThat(extractRequest.getPath()).isEqualTo("/files/extract/zip?path="+ STORAGE_NODE.getPath());

    final RecordedRequest deleteRequest = storageMockWebServer.takeRequest();
    assertThat(deleteRequest.getPath()).isEqualTo("/files/delete");
    assertThat(deleteRequest.getBody().readString(Charsets.UTF_8)).isEqualTo(String.format("[\"%s\"]", STORAGE_NODE.getPath()));
  }
}
