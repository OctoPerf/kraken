package com.octoperf.kraken.storage.server;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.storage.entity.StorageNode;
import com.octoperf.kraken.storage.entity.StorageNodeTest;
import com.octoperf.kraken.storage.entity.StorageWatcherEvent;
import com.octoperf.kraken.storage.entity.StorageWatcherEventType;
import com.octoperf.kraken.storage.file.StorageService;
import com.octoperf.kraken.storage.file.StorageServiceBuilder;
import com.octoperf.kraken.tests.utils.TestUtils;
import com.octoperf.kraken.tests.web.security.AuthControllerTest;
import com.octoperf.kraken.tools.sse.SSEService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static com.octoperf.kraken.storage.entity.StorageNodeType.DIRECTORY;
import static com.octoperf.kraken.storage.entity.StorageWatcherEventTest.STORAGE_WATCHER_EVENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class StorageControllerTest extends AuthControllerTest {

  @MockBean
  StorageService service;
  @MockBean
  StorageServiceBuilder builder;
  @MockBean
  SSEService sse;

  @BeforeEach
  public void setUp() throws IOException {
    super.setUp();
    given(builder.build(userOwner())).willReturn(service);
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(StorageController.class);
  }

  @Test
  public void shouldGetStaticFolder() {
    given(service.getFileResource(applicationId + "/"))
        .willReturn(Mono.just(new FileSystemResource("testDir/kraken.zip")));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/static")
            .pathSegment(applicationId)
            .pathSegment(projectId)
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals(HttpHeaders.CONTENT_LENGTH, "2328645")
        .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, "application/zip");
  }

  @Test
  public void shouldGetStaticTxt() {
    given(service.getFileResource(applicationId + "/sub/static.txt"))
        .willReturn(Mono.just(new FileSystemResource("testDir/sub/static.txt")));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/static")
            .pathSegment(applicationId)
            .pathSegment(projectId)
            .pathSegment("sub/static.txt")
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals(HttpHeaders.CONTENT_LENGTH, "16")
        .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, "text/plain")
        .expectBody(String.class)
        .isEqualTo("Spring Framework");
  }

  @Test
  public void shouldGetStaticHAR() {
    given(service.getFileResource(applicationId + "/sub/static.har"))
        .willReturn(Mono.just(new FileSystemResource("testDir/sub/static.har")));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/static")
            .pathSegment(applicationId)
            .pathSegment(projectId)
            .pathSegment("sub/static.har")
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals(HttpHeaders.CONTENT_LENGTH, "2")
        .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
        .expectBody(String.class)
        .isEqualTo("{}");
  }

  @Test
  public void shouldGetStaticImage() {
    given(service.getFileResource(applicationId + "/sub/static.svg"))
        .willReturn(Mono.just(new FileSystemResource("testDir/sub/static.svg")));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/static")
            .pathSegment(applicationId)
            .pathSegment(projectId)
            .pathSegment("sub/static.svg")
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals(HttpHeaders.CONTENT_LENGTH, "11")
        .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, "image/svg+xml")
        .expectBody(String.class)
        .isEqualTo("<svg></svg>");
  }

  @Test
  public void shouldListNodes() {
    final var path = "path";
    given(service.list())
        .willReturn(Flux.just(StorageNode.builder().path(path).depth(0).length(0L).lastModified(0L).type(DIRECTORY).build()));

    webTestClient.get()
        .uri("/files/list")
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$[0].path").isEqualTo(path);
  }

  @Test
  public void shouldListNodesForbidden() {
    webTestClient.get()
        .uri("/files/list")
        .header("Authorization", "Bearer no-role-token")
        .exchange()
        .expectStatus().is4xxClientError();
  }

  @Test
  public void shouldGetNode() {
    final var filename = "file.txt";
    given(service.get(filename))
        .willReturn(Mono.just(StorageNode.builder().path(filename).depth(0).length(0L).lastModified(0L).type(DIRECTORY).build()));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/get")
            .queryParam("path", filename)
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.path").isEqualTo(filename);
  }

  @Test
  public void shouldDelete() {
    final var paths = Arrays.asList("toto/file.txt");
    given(service.delete(paths))
        .willReturn(Flux.just(
            StorageWatcherEvent.builder()
                .node(StorageNode.builder().path("toto/file.txt").depth(0).length(0L).lastModified(0L).type(DIRECTORY).build())
                .type(StorageWatcherEventType.CREATE)
                .owner(userOwner())
                .build()
        ));

    webTestClient.post()
        .uri("/files/delete")
        .body(BodyInserters.fromValue(paths))
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public void shouldDeleteFail() {
    final var paths = Arrays.asList("toto/file.txt");
    given(service.delete(paths))
        .willReturn(Flux.error(new IllegalArgumentException("Failed to delete")));

    webTestClient.post()
        .uri("/files/delete")
        .header("Authorization", "Bearer user-token")
        .body(BodyInserters.fromValue(paths))
        .exchange()
        .expectStatus().is5xxServerError();
  }

  @Test
  public void shouldSetFile() throws IOException {
    final var path = "toto";
    given(service.setFile(any(), any()))
        .willReturn(Flux.just(STORAGE_WATCHER_EVENT));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/file")
            .queryParam("path", path)
            .build())
        .header("Authorization", "Bearer user-token")
        .body(BodyInserters.fromMultipartData("file", new UrlResource("file", "testDir/testupload.txt")))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(StorageWatcherEvent.class)
        .isEqualTo(ImmutableList.of(STORAGE_WATCHER_EVENT));
  }

  @Test
  public void shouldSetZip() throws IOException {
    final var path = "toto";
    given(service.setZip(any(), any()))
        .willReturn(Flux.just(STORAGE_WATCHER_EVENT));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/zip")
            .queryParam("path", path)
            .build())
        .header("Authorization", "Bearer user-token")
        .body(BodyInserters.fromMultipartData("file", new UrlResource("file", "testDir/kraken.zip")))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(StorageWatcherEvent.class)
        .isEqualTo(ImmutableList.of(STORAGE_WATCHER_EVENT));
  }

  @Test
  public void shouldSetDirectory() {
    final var path = "someDir";
    given(service.setDirectory(path))
        .willReturn(Flux.just(STORAGE_WATCHER_EVENT));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/directory")
            .queryParam("path", path)
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(StorageWatcherEvent.class)
        .isEqualTo(ImmutableList.of(STORAGE_WATCHER_EVENT));
  }

  @Test
  public void shouldGetFile() {
    final var content = "File getContent";
    given(service.getFileInputStream(""))
        .willReturn(Mono.just(new ByteArrayInputStream(content.getBytes(Charsets.UTF_8))));
    given(service.getFileName(""))
        .willReturn("test.txt");

    webTestClient.get()
        .uri("/files/get/file")
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test.txt\"")
        .expectBody(String.class)
        .isEqualTo(content);
  }

  @Test
  public void shouldRenameFile() {
    final var oldName = "oldName.txt";
    final var newName = "newName.txt";
    final var node = StorageNode.builder().path(newName).depth(0).length(0L).lastModified(0L).type(DIRECTORY).build();
    given(service.rename("", oldName, newName))
        .willReturn(Flux.just(STORAGE_WATCHER_EVENT));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/rename")
            .queryParam("oldName", oldName)
            .queryParam("newName", newName)
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(StorageWatcherEvent.class)
        .isEqualTo(ImmutableList.of(STORAGE_WATCHER_EVENT));
  }

  @Test
  public void shouldGetDirectory() {
    final var content = "File getContent";
    given(service.getFileInputStream(""))
        .willReturn(Mono.just(new ByteArrayInputStream(content.getBytes(Charsets.UTF_8))));
    given(service.getFileName(""))
        .willReturn("test.zip");

    webTestClient.get()
        .uri("/files/get/file?path=")
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test.zip\"")
        .expectBody(String.class)
        .isEqualTo(content);
  }

  @Test
  public void shouldSetContent() {
    final var path = "toto/file.txt";
    final var content = "Test content";
    given(service.setContent(path, content))
        .willReturn(Flux.just(STORAGE_WATCHER_EVENT));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/content")
            .queryParam("path", path)
            .build())
        .header("Authorization", "Bearer user-token")
        .body(BodyInserters.fromValue(content))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(StorageWatcherEvent.class)
        .isEqualTo(ImmutableList.of(STORAGE_WATCHER_EVENT));
  }

  @Test
  public void shouldGetContent() {
    final var path = "toto/file.txt";
    given(service.getContent(path))
        .willReturn(Mono.just("some getContent"));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/get/content")
            .queryParam("path", path)
            .build())
        .accept(MediaType.TEXT_PLAIN)
        .header("Content-type", MediaType.TEXT_PLAIN_VALUE)
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("text/plain;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo("some getContent");
  }


  @Test
  public void shouldGetJson() {
    final var path = "toto/file.json";
    given(service.getContent(path))
        .willReturn(Mono.just("{}"));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/get/json")
            .queryParam("path", path)
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer user-token")
        .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("application/json;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo("{}");
  }

  @Test
  public void shouldGetContents() {
    final var paths = ImmutableList.of("toto/file1.txt", "toto/file2.txt");
    given(service.getContent(paths))
        .willReturn(Flux.just("{\"key1\": \"value1\"}", "{\"key2\": \"value2\"}"));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/list/json")
            .build())
        .body(BodyInserters.fromValue(paths))
        .header("Authorization", "Bearer user-token")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("application/json;charset=UTF-8")
        .expectBody(String.class)
        .isEqualTo("[{\"key1\": \"value1\"}, {\"key2\": \"value2\"}]");
  }


  @Test
  public void shouldWatch() {
    final var flux = Flux.just(STORAGE_WATCHER_EVENT, STORAGE_WATCHER_EVENT);
    final var sseFlux = Flux.just(ServerSentEvent.builder(STORAGE_WATCHER_EVENT).build(), ServerSentEvent.<StorageWatcherEvent>builder().comment("keep alive").build(), ServerSentEvent.builder(STORAGE_WATCHER_EVENT).build());

    given(service.watch(""))
        .willReturn(flux);

    given(sse.keepAlive(flux)).willReturn(sseFlux);

    final var result = webTestClient.get()
        .uri("/files/watch")
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
        .expectBody()
        .returnResult();

    final var body = new String(Optional.ofNullable(result.getResponseBody()).orElse(new byte[0]), Charsets.UTF_8);
    Assertions.assertThat(body).isEqualTo("data:{\"node\":{\"path\":\"path\",\"type\":\"DIRECTORY\",\"depth\":0,\"length\":0,\"lastModified\":0},\"type\":\"CREATE\",\"owner\":{\"userId\":\"\",\"projectId\":\"\",\"applicationId\":\"\",\"roles\":[],\"type\":\"PUBLIC\"}}\n" +
        "\n" +
        ":keep alive\n" +
        "\n" +
        "data:{\"node\":{\"path\":\"path\",\"type\":\"DIRECTORY\",\"depth\":0,\"length\":0,\"lastModified\":0},\"type\":\"CREATE\",\"owner\":{\"userId\":\"\",\"projectId\":\"\",\"applicationId\":\"\",\"roles\":[],\"type\":\"PUBLIC\"}}\n" +
        "\n");
  }

  @Test
  public void shouldFindFiles() {
    given(service.find("", Integer.MAX_VALUE, ".*"))
        .willReturn(Flux.just(StorageNodeTest.STORAGE_NODE));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/find")
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$[0].path").isEqualTo(StorageNodeTest.STORAGE_NODE.getPath());
  }

  @Test
  public void shouldCopyFiles() {
    final var paths = Arrays.asList("path1", "path2");
    final var destination = "destination";
    given(service.copy(paths, destination))
        .willReturn(Flux.just(STORAGE_WATCHER_EVENT));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/copy")
            .queryParam("destination", destination)
            .build())
        .header("Authorization", "Bearer user-token")
        .body(BodyInserters.fromValue(paths))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(StorageWatcherEvent.class)
        .isEqualTo(ImmutableList.of(STORAGE_WATCHER_EVENT));
  }

  @Test
  public void shouldMoveFiles() {
    final var paths = Arrays.asList("path1", "path2");
    final var destination = "destination";
    given(service.move(paths, destination))
        .willReturn(Flux.just(STORAGE_WATCHER_EVENT));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/move")
            .queryParam("destination", destination)
            .build())
        .header("Authorization", "Bearer user-token")
        .body(BodyInserters.fromValue(paths))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(StorageWatcherEvent.class)
        .isEqualTo(ImmutableList.of(STORAGE_WATCHER_EVENT));
  }

  @Test
  public void shouldFilterExisting() {
    final var nodes = ImmutableList.of(
        StorageNode.builder()
            .path("visitorTest/dir1")
            .type(DIRECTORY)
            .depth(1)
            .lastModified(0L)
            .length(0L)
            .build(),
        StorageNode.builder()
            .path("visitorTest/dir2")
            .type(DIRECTORY)
            .depth(1)
            .lastModified(0L)
            .length(0L)
            .build()
    );

    given(service.filterExisting(nodes))
        .willReturn(Flux.fromIterable(nodes));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/filter/existing")
            .build())
        .header("Authorization", "Bearer user-token")
        .body(BodyInserters.fromValue(nodes))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$[0].path").isEqualTo(nodes.get(0).getPath())
        .jsonPath("$[1].path").isEqualTo(nodes.get(1).getPath())
        .jsonPath("$.length()").isEqualTo(2);
  }

  @Test
  public void shouldExtractZip() {
    final var path = "path/archive.zip";
    given(service.extractZip(path))
        .willReturn(Flux.just(STORAGE_WATCHER_EVENT));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/extract/zip")
            .queryParam("path", path)
            .build())
        .header("Authorization", "Bearer user-token")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(StorageWatcherEvent.class)
        .isEqualTo(ImmutableList.of(STORAGE_WATCHER_EVENT));
  }
}
