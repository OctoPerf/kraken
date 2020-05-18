package com.kraken.storage.entity;

import org.junit.Test;

import static com.kraken.storage.entity.StorageNodeType.DIRECTORY;
import static com.kraken.tests.utils.TestUtils.shouldPassAll;
import static com.kraken.tests.utils.TestUtils.shouldPassToString;
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
    shouldPassAll(STORAGE_NODE);
    shouldPassToString(StorageNode.builder());
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
