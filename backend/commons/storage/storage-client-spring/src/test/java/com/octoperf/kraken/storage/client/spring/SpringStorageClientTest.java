package com.octoperf.kraken.storage.client.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.Application;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.entity.*;
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

import static com.octoperf.kraken.storage.entity.StorageNodeTest.STORAGE_NODE;
import static com.octoperf.kraken.storage.entity.StorageNodeType.DIRECTORY;
import static com.octoperf.kraken.storage.entity.StorageNodeType.FILE;
import static com.octoperf.kraken.storage.entity.StorageWatcherEventTest.STORAGE_WATCHER_EVENT;
import static com.octoperf.kraken.storage.entity.StorageWatcherEventType.CREATE;
import static com.octoperf.kraken.storage.entity.StorageWatcherEventType.MODIFY;
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
    client = new SpringStorageClientBuilder(ImmutableList.of(), storageServiceBuilder, jsonMapper, yamlMapper).build(AuthenticatedClientBuildOrder.NOOP).block();
  }

  @Test
  public void shouldInit() {
    given(storageService.init()).willReturn(Mono.empty());
    final var response = client.init().block();
    assertThat(response).isNull();
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
    given(storageService.setDirectory("path")).willReturn(Flux.just(STORAGE_WATCHER_EVENT,
        StorageWatcherEvent.builder().node(directoryNode).owner(Owner.PUBLIC).type(CREATE).build()));
    final var response = client.createFolder("path").block();
    assertThat(response).isEqualTo(directoryNode);
  }

  @Test
  public void shouldDelete() {
    given(storageService.delete(ImmutableList.of("path"))).willReturn(Flux.just(STORAGE_WATCHER_EVENT));
    final var response = client.delete("path").block();
    assertThat(response).isEqualTo(true);
  }

  @Test
  public void shouldSetContent() {
    final var fileNode = StorageNode.builder()
        .depth(1)
        .path("path/result.son")
        .type(FILE)
        .length(0L)
        .lastModified(0L)
        .build();
    given(storageService.setContent(fileNode.getPath(), "content")).willReturn(Flux.just(STORAGE_WATCHER_EVENT,
        StorageWatcherEvent.builder().node(fileNode).owner(Owner.PUBLIC).type(CREATE).build()));

    final var response = client.setContent(fileNode.getPath(), "content").block();
    assertThat(response).isEqualTo(fileNode);
  }

  @Test
  public void shouldWatch() {
    given(storageService.watch("")).willReturn(Flux.just(
        STORAGE_WATCHER_EVENT,
        STORAGE_WATCHER_EVENT,
        STORAGE_WATCHER_EVENT,
        STORAGE_WATCHER_EVENT
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
    given(storageService.setContent(fileNode.getPath(), jsonMapper.writeValueAsString(STORAGE_WATCHER_EVENT))).willReturn(Flux.just(StorageWatcherEvent.builder()
        .type(MODIFY)
        .owner(Owner.PUBLIC)
        .node(fileNode)
        .build()));
    final var response = client.setJsonContent(fileNode.getPath(), STORAGE_WATCHER_EVENT).block();
    assertThat(response).isEqualTo(fileNode);
  }

  @Test
  public void shouldGetJsonContent() throws IOException {
    given(storageService.getContent("path")).willReturn(Mono.just(jsonMapper.writeValueAsString(STORAGE_WATCHER_EVENT)));
    final var response = client.getJsonContent("path", StorageWatcherEvent.class).block();
    assertThat(response).isEqualTo(STORAGE_WATCHER_EVENT);
  }

  @Test
  public void shouldGetYamlContent() throws IOException {
    given(storageService.getContent("path")).willReturn(Mono.just(yamlMapper.writeValueAsString(STORAGE_NODE)));
    final var response = client.getYamlContent("path", StorageNode.class).block();
    assertThat(response).isEqualTo(STORAGE_NODE);
  }

  @Test
  public void shouldFind() throws IOException {
    given(storageService.find("path", 42, ".*")).willReturn(Flux.just(STORAGE_NODE));
    final var response = client.find("path", 42, ".*").collectList().block();
    assertThat(response).isNotNull();
    assertThat(response).isEqualTo(ImmutableList.of(STORAGE_NODE));
  }
}
