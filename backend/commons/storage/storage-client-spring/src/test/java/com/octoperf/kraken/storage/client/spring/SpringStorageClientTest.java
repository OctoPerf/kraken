package com.octoperf.kraken.storage.client.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.Application;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.entity.StorageNode;
import com.octoperf.kraken.storage.entity.StorageWatcherEvent;
import com.octoperf.kraken.storage.entity.StorageWatcherEventTest;
import com.octoperf.kraken.storage.file.StorageService;
import com.octoperf.kraken.storage.file.StorageServiceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static com.octoperf.kraken.storage.entity.StorageNodeType.DIRECTORY;
import static com.octoperf.kraken.storage.entity.StorageNodeType.FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class SpringStorageClientTest {

  private StorageClient client;

  @Autowired
  @Qualifier("yamlObjectMapper")
  ObjectMapper yamlMapper;
  @Autowired
  ObjectMapper jsonMapper;
  @MockBean
  StorageServiceBuilder storageServiceBuilder;
  @MockBean
  StorageService storageService;

  @BeforeEach
  public void before() {
    given(storageServiceBuilder.build(any())).willReturn(storageService);
    client = new SpringStorageClientBuilder(ImmutableList.of(), storageServiceBuilder, jsonMapper, yamlMapper).mode(AuthenticationMode.NOOP).build().block();
  }

  @Test
  public void shouldCreateFolder() {
    final var directoryNode = StorageNode.builder()
        .depth(0)
        .path("path")
        .type(DIRECTORY)
        .length(0L)
        .lastModified(0L)
        .build();
    given(storageService.setDirectory("path")).willReturn(Mono.just(directoryNode));
    final var response = client.createFolder("path").block();
    assertThat(response).isEqualTo(directoryNode);
  }

  @Test
  public void shouldDelete() {
    given(storageService.delete(ImmutableList.of("path"))).willReturn(Flux.just(true));
    final var response = client.delete("path").block();
    assertThat(response).isEqualTo(true);
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
    given(storageService.setContent("path", "content")).willReturn(Mono.just(fileNode));
    final var response = client.setContent("path", "content").block();
    assertThat(response).isEqualTo(fileNode);
  }

  @Test
  public void shouldWatch() {
    given(storageService.watch("")).willReturn(Flux.just(
        StorageWatcherEventTest.STORAGE_WATCHER_EVENT,
        StorageWatcherEventTest.STORAGE_WATCHER_EVENT,
        StorageWatcherEventTest.STORAGE_WATCHER_EVENT,
        StorageWatcherEventTest.STORAGE_WATCHER_EVENT
    ));
    final var response = client.watch().collectList().block();
    assertThat(response).isNotNull();
    assertThat(response.size()).isEqualTo(4);
  }

  @Test
  public void shouldGetContent() {
    given(storageService.getContent("path")).willReturn(Mono.just("content"));
    final var response = client.getContent("path").block();
    assertThat(response).isEqualTo("content");
  }

  @Test
  public void shouldSetJsonContent() throws IOException {
    final var fileNode = StorageNode.builder()
        .depth(1)
        .path("path/result.son")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();
    given(storageService.setContent("path", jsonMapper.writeValueAsString(StorageWatcherEventTest.STORAGE_WATCHER_EVENT))).willReturn(Mono.just(fileNode));
    final var response = client.setJsonContent("path", StorageWatcherEventTest.STORAGE_WATCHER_EVENT).block();
    assertThat(response).isEqualTo(fileNode);
  }

  @Test
  public void shouldGetJsonContent() throws IOException {
    given(storageService.getContent("path")).willReturn(Mono.just(jsonMapper.writeValueAsString(StorageWatcherEventTest.STORAGE_WATCHER_EVENT)));
    final var response = client.getJsonContent("path", StorageWatcherEvent.class).block();
    assertThat(response).isEqualTo(StorageWatcherEventTest.STORAGE_WATCHER_EVENT);
  }

  @Test
  public void shouldGetYamlContent() throws IOException {
    given(storageService.getContent("path")).willReturn(Mono.just(yamlMapper.writeValueAsString(StorageWatcherEventTest.STORAGE_WATCHER_EVENT)));
    final var response = client.getYamlContent("path", StorageWatcherEvent.class).block();
    assertThat(response).isEqualTo(StorageWatcherEventTest.STORAGE_WATCHER_EVENT);
  }

}
