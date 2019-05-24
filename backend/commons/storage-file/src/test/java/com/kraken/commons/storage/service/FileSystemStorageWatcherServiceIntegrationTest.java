package com.kraken.commons.storage.service;

import com.google.common.base.Charsets;
import com.kraken.commons.rest.configuration.ApplicationProperties;
import com.kraken.commons.rest.configuration.ApplicationPropertiesTestConfiguration;
import com.kraken.commons.storage.TestConfiguration;
import com.kraken.commons.storage.entity.StorageNode;
import com.kraken.commons.storage.entity.StorageWatcherEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.of;
import static com.kraken.commons.storage.entity.StorageNodeType.*;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.FileSystemUtils.deleteRecursively;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationPropertiesTestConfiguration.class, TestConfiguration.class})
public class FileSystemStorageWatcherServiceIntegrationTest {

  @Autowired
  StorageWatcherService service;

  @Autowired
  ApplicationProperties applicationProperties;

  @Test
  public void shouldWatchTestDir() throws IOException, InterruptedException {
    final var watch = service.watch();
    final var events = new ArrayList<StorageWatcherEvent>();
    final var subscription = watch.subscribe(events::add);
    final var currentPath = this.applicationProperties.getData().resolve("test/toto");
    final var file = currentPath.toFile();
    assertThat(file.mkdirs()).isTrue();
    sleep(1000);
    final var textPath = currentPath.resolve("the-file-name.txt");
    Files.write(textPath, Arrays.asList("The first line", "The second line"), Charsets.UTF_8);
    sleep(1000);
    Files.write(textPath, Collections.singletonList("Other getContent"), Charsets.UTF_8);
    sleep(1000);
    assertThat(textPath.toFile().renameTo(currentPath.resolve("the-new-file-name.txt").toFile())).isTrue();
    sleep(1000);
    deleteRecursively(this.applicationProperties.getData().resolve("test"));
    sleep(1000);
    subscription.dispose();

    assertThat(events
        .stream()
        .map(StorageWatcherEvent::getNode)
        .map(StorageNode::getPath)
        .collect(Collectors.toList()))
        .isEqualTo(of(
            "test",
            "test/toto",
            "test/toto/the-file-name.txt",
            "test/toto/the-file-name.txt",
            "test/toto/the-file-name.txt",
            "test/toto/the-new-file-name.txt",
            "test/toto/the-new-file-name.txt",
            "test/toto",
            "test"
        ));

    assertThat(events
        .stream()
        .map(StorageWatcherEvent::getNode)
        .map(StorageNode::getType)
        .collect(Collectors.toList()))
        .isEqualTo(of(
            DIRECTORY,
            DIRECTORY,
            FILE,
            FILE,
            NONE,
            FILE,
            NONE,
            NONE,
            NONE
        ));


    assertThat(events
        .stream()
        .map(StorageWatcherEvent::getEvent)
        .collect(Collectors.toList()))
        .isEqualTo(of(
            "CREATE",
            "CREATE",
            "CREATE",
            "MODIFY",
            "DELETE",
            "CREATE",
            "DELETE",
            "DELETE",
            "DELETE"
        ));
  }
}
