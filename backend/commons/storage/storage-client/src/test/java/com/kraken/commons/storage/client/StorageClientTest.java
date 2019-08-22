package com.kraken.commons.storage.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.kraken.commons.analysis.entity.Result;
import com.kraken.commons.analysis.entity.ResultStatus;
import com.kraken.commons.storage.entity.StorageNode;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import okio.Okio;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.zip.CRC32;

import static com.kraken.commons.analysis.entity.ResultType.RUN;
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
  public void shouldGetFile() throws InterruptedException, IOException {
    final var testFile = "testDir/test.xml";
    final var outFile = "testDir/out.xml";

    final var buffer = new Buffer();
    buffer.writeAll(Okio.source(new File(testFile)));

    storageMockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
            .setChunkedBody(buffer, 128)
    );

    final Flux<DataBuffer> flux = client.getFile(Optional.empty());
    DataBufferUtils.write(flux, new FileOutputStream(outFile).getChannel())
        .map(DataBufferUtils::release).blockLast();

    try {
      final var test = Files.readString(Paths.get(testFile), Charsets.UTF_8);
      final var out = Files.readString(Paths.get(outFile), Charsets.UTF_8);
      Assertions.assertThat(test).isEqualTo(out);
      System.out.println(out);
      Files.delete(Paths.get(outFile));
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }

    final RecordedRequest recordedRequest = storageMockWebServer.takeRequest();
    assertThat(recordedRequest.getPath()).startsWith("/files/get/file?path=");
  }
}
