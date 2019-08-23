package com.kraken.storage.file;

import com.kraken.tools.configuration.properties.ApplicationProperties;
import com.kraken.tools.configuration.properties.ApplicationPropertiesTestConfiguration;
import com.kraken.storage.TestConfiguration;
import com.kraken.storage.entity.StorageNode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import static com.kraken.storage.entity.StorageNodeType.FILE;
import static com.kraken.storage.entity.StorageNodeType.NONE;
import static com.kraken.test.utils.TestUtils.shouldPassNPE;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationPropertiesTestConfiguration.class, TestConfiguration.class})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PathToStorageNodeTest {

  @Autowired
  Function<Path, StorageNode> service;

  @Autowired
  ApplicationProperties applicationProperties;

  @Test
  public void shouldPassTestUtils() {
    shouldPassNPE(PathToStorageNode.class);
  }

  @Test
  public void shouldConvertExistingPath() {
    Assert.assertEquals(StorageNode.builder()
        .path("README.md")
        .type(FILE)
        .depth(0)
        .length(applicationProperties.getData().resolve("README.md").toFile().length())
        .lastModified(applicationProperties.getData().resolve("README.md").toFile().lastModified())
        .build(), service.apply(applicationProperties.getData().resolve("README.md")));
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
