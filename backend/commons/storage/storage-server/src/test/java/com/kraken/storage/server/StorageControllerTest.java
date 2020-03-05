package com.kraken.storage.server;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageNodeTest;
import com.kraken.storage.entity.StorageWatcherEvent;
import com.kraken.storage.file.StorageService;
import com.kraken.storage.file.StorageWatcherService;
import com.kraken.tools.configuration.jackson.MediaTypes;
import com.kraken.tools.sse.SSEService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static com.kraken.storage.entity.StorageNodeType.DIRECTORY;
import static com.kraken.storage.entity.StorageWatcherEventTest.STORAGE_WATCHER_EVENT;
import static com.kraken.test.utils.TestUtils.shouldPassNPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {StorageController.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class StorageControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  StorageService service;

  @MockBean
  StorageWatcherService watcher;

  @MockBean
  SSEService sse;

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(StorageController.class);
  }

  @Test
  public void shouldListNodes() {
    final var path = "path";
    given(service.list())
        .willReturn(Flux.just(StorageNode.builder().path(path).depth(0).length(0L).lastModified(0L).type(DIRECTORY).build()));

    webTestClient.get()
        .uri("/files/list")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$[0].path").isEqualTo(path);
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
        .willReturn(Flux.just(true));

    webTestClient.post()
        .uri("/files/delete")
        .body(BodyInserters.fromValue(paths))
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
        .body(BodyInserters.fromValue(paths))
        .exchange()
        .expectStatus().is5xxServerError();
  }

  @Test
  public void shouldSetFile() throws IOException {
    final var path = "toto";
    given(service.setFile(any(), any()))
        .willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/file")
            .queryParam("path", path)
            .build())
        .body(BodyInserters.fromMultipartData("file", new UrlResource("file", "testDir/testupload.txt")))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(StorageNode.class)
        .isEqualTo(StorageNodeTest.STORAGE_NODE);
  }

  @Test
  public void shouldSetZip() throws IOException {
    final var path = "toto";
    given(service.setZip(any(), any()))
        .willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/zip")
            .queryParam("path", path)
            .build())
        .body(BodyInserters.fromMultipartData("file", new UrlResource("file", "testDir/kraken.zip")))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(StorageNode.class)
        .isEqualTo(StorageNodeTest.STORAGE_NODE);
  }

  @Test
  public void shouldSetDirectory() {
    final var path = "someDir";
    given(service.setDirectory(path))
        .willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/directory")
            .queryParam("path", path)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(StorageNode.class)
        .isEqualTo(StorageNodeTest.STORAGE_NODE);
  }

  @Test
  public void shouldGetFile() {
    final var content = "File getContent";
    given(service.getFile(""))
        .willReturn(Mono.just(new ByteArrayInputStream(content.getBytes(Charsets.UTF_8))));
    given(service.getFileName(""))
        .willReturn("test.txt");

    webTestClient.get()
        .uri("/files/get/file")
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
        .willReturn(Mono.just(node));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/rename")
            .queryParam("oldName", oldName)
            .queryParam("newName", newName)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(StorageNode.class)
        .isEqualTo(node);
  }

  @Test
  public void shouldGetDirectory() {
    final var content = "File getContent";
    given(service.getFile(""))
        .willReturn(Mono.just(new ByteArrayInputStream(content.getBytes(Charsets.UTF_8))));
    given(service.getFileName(""))
        .willReturn("test.zip");

    webTestClient.get()
        .uri("/files/get/file?path=")
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
        .willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/content")
            .queryParam("path", path)
            .build())
        .body(BodyInserters.fromValue(content))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(StorageNode.class)
        .isEqualTo(StorageNodeTest.STORAGE_NODE);
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

    given(watcher.watch())
        .willReturn(flux);

    given(sse.keepAlive(flux)).willReturn(sseFlux);

    final var result = webTestClient.get()
        .uri("/files/watch")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
        .expectBody()
        .returnResult();

    final var body = new String(Optional.ofNullable(result.getResponseBody()).orElse(new byte[0]), Charsets.UTF_8);
    Assertions.assertThat(body).isEqualTo("data:{\"node\":{\"path\":\"path\",\"type\":\"DIRECTORY\",\"depth\":0,\"length\":0,\"lastModified\":0},\"event\":\"Event\"}\n" +
        "\n" +
        ":keep alive\n" +
        "\n" +
        "data:{\"node\":{\"path\":\"path\",\"type\":\"DIRECTORY\",\"depth\":0,\"length\":0,\"lastModified\":0},\"event\":\"Event\"}\n" +
        "\n");
  }

  @Test
  public void shouldFindFiles() {
    given(service.find("", Integer.MAX_VALUE, ".*"))
        .willReturn(Flux.just(StorageNodeTest.STORAGE_NODE));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/find")
            .build())
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
        .willReturn(Flux.just(StorageNodeTest.STORAGE_NODE));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/copy")
            .queryParam("destination", destination)
            .build())
        .body(BodyInserters.fromValue(paths))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$[0].path").isEqualTo(StorageNodeTest.STORAGE_NODE.getPath());
  }

  @Test
  public void shouldMoveFiles() {
    final var paths = Arrays.asList("path1", "path2");
    final var destination = "destination";
    given(service.move(paths, destination))
        .willReturn(Flux.just(StorageNodeTest.STORAGE_NODE));

    webTestClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/move")
            .queryParam("destination", destination)
            .build())
        .body(BodyInserters.fromValue(paths))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$[0].path").isEqualTo(StorageNodeTest.STORAGE_NODE.getPath());
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
        .willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/extract/zip")
            .queryParam("path", path)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.path").isEqualTo(StorageNodeTest.STORAGE_NODE.getPath());
  }
}
