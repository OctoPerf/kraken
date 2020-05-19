package com.kraken.storage.file;

import com.kraken.Application;
import com.kraken.storage.entity.StorageNode;
import com.kraken.config.api.ApplicationProperties;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import static com.kraken.storage.entity.StorageNodeType.FILE;
import static com.kraken.storage.entity.StorageNodeType.NONE;
import static com.kraken.tests.utils.TestUtils.shouldPassNPE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class FileSystemPathToStorageNodeTest {

  FileSystemPathToStorageNode service;

  @Autowired
  ApplicationProperties krakenProperties;

  @BeforeEach
  public void setUp() {
    service = new FileSystemPathToStorageNode(Path.of(krakenProperties.getData(), "public"));
  }

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(FileSystemPathToStorageNode.class);
  }

  @Test
  public void shouldConvertExistingPath() {
    final var data = Path.of(krakenProperties.getData(), "public", "README.md");
    Assert.assertEquals(StorageNode.builder()
        .path("README.md")
        .type(FILE)
        .depth(0)
        .length(data.toFile().length())
        .lastModified(data.toFile().lastModified())
        .build(), service.apply(data));
  }

  public void shouldFail() {
    Assert.assertEquals(StorageNode.builder()
        .path("niet/other.md")
        .type(NONE)
        .depth(1)
        .length(0L)
        .lastModified(0L)
        .build(), service.apply(Paths.get("niet/other.md")));
  }
}
