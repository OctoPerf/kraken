package com.octoperf.kraken.storage.file;

import com.octoperf.kraken.storage.entity.StorageNode;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.octoperf.kraken.storage.entity.StorageNodeType.FILE;
import static com.octoperf.kraken.storage.entity.StorageNodeType.NONE;

public class FileSystemPathToStorageNodeTest {

  FileSystemPathToStorageNode service;

  Path root;

  @BeforeEach
  public void setUp() {
    root = Paths.get("testDir");
    service = new FileSystemPathToStorageNode(root);
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(FileSystemPathToStorageNode.class);
  }

  @Test
  public void shouldConvertExistingPath() {
    final var data = root.resolve(Path.of("public", "README.md"));
    Assert.assertEquals(StorageNode.builder()
        .path("public/README.md")
        .type(FILE)
        .depth(1)
        .length(data.toFile().length())
        .lastModified(data.toFile().lastModified())
        .build(), service.apply(data));
  }

  @Test
  public void shouldFail() {
    Assert.assertEquals(StorageNode.builder()
        .path("../niet/other.md")
        .type(NONE)
        .depth(2)
        .length(0L)
        .lastModified(0L)
        .build(), service.apply(Paths.get("niet/other.md")));
  }

  @Test
  public void shouldHomeFail() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      final Path root = Paths.get("/root");
      final FileSystemPathToStorageNode service = new FileSystemPathToStorageNode(root);
      service.apply(Paths.get("home"));
    });
  }
}
