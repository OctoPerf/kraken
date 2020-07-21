package com.octoperf.kraken.storage.entity;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static com.octoperf.kraken.storage.entity.StorageNodeType.DIRECTORY;
import static org.assertj.core.api.Assertions.assertThat;

public class StorageNodeTest {

  public static final StorageNode STORAGE_NODE = StorageNode.builder()
      .depth(0)
      .path("path")
      .type(DIRECTORY)
      .length(0L)
      .lastModified(0L)
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(STORAGE_NODE);
    TestUtils.shouldPassToString(StorageNode.builder());
  }

  @Test
  public void shouldNotBeRoot() {
    assertThat(STORAGE_NODE.notRoot()).isTrue();
  }

  @Test
  public void shouldBeRoot() {
    assertThat( StorageNode.builder()
        .depth(0)
        .path("")
        .type(DIRECTORY)
        .length(0L)
        .lastModified(0L)
        .build().notRoot()).isFalse();
  }
}
