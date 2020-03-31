package com.kraken.storage.file;

import com.google.common.base.Charsets;
import com.kraken.Application;
import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageWatcherEvent;
import com.kraken.config.api.ApplicationProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.of;
import static com.kraken.storage.entity.StorageNodeType.*;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.FileSystemUtils.deleteRecursively;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class FileSystemStorageWatcherServiceIntegrationTest {
  @Autowired
  StorageWatcherService service;
  @Autowired
  ApplicationProperties krakenProperties;

  @Test
  public void shouldWatchTestDir() throws IOException, InterruptedException {
    final var watch = service.watch();
    final var events = new ArrayList<StorageWatcherEvent>();
    final var subscription = watch.subscribe(events::add);
    final var data = Paths.get(krakenProperties.getData());
    final var currentPath = data.resolve("test/toto");
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
    deleteRecursively(data.resolve("test"));
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

  @Test
  public void shouldWatchSubDir() throws IOException, InterruptedException {
    final var data = Paths.get(krakenProperties.getData());
    final var root = data.resolve("test2");
    final var otherPath = root.resolve("other");
    final var currentPath = root.resolve("toto");
    final var watch = service.watch("test2/toto");
    final var events = new ArrayList<StorageWatcherEvent>();
    final var subscription = watch.subscribe(events::add);
    final var file = currentPath.toFile();
    assertThat(file.mkdirs()).isTrue();
    assertThat(otherPath.toFile().mkdirs()).isTrue();
    sleep(1000);
    final var textPath = otherPath.resolve("the-file-name.txt");
    Files.write(textPath, Arrays.asList("The first line", "The second line"), Charsets.UTF_8);
    sleep(1000);
    assertThat(textPath.toFile().renameTo(currentPath.resolve("the-new-file-name.txt").toFile())).isTrue();
    sleep(1000);
    // Deleting the root directly won't sent an event for the deletion of test2/toto, only for test2
    deleteRecursively(currentPath);
    deleteRecursively(root);
    sleep(1000);
    subscription.dispose();

    assertThat(events
        .stream()
        .map(StorageWatcherEvent::getNode)
        .map(StorageNode::getPath)
        .collect(Collectors.toList()))
        .isEqualTo(of(
            "test2/toto",
            "test2/toto/the-new-file-name.txt",
            "test2/toto/the-new-file-name.txt",
            "test2/toto"
        ));

    assertThat(events
        .stream()
        .map(StorageWatcherEvent::getNode)
        .map(StorageNode::getType)
        .collect(Collectors.toList()))
        .isEqualTo(of(
            DIRECTORY,
            FILE,
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
            "DELETE",
            "DELETE"
        ));
  }

}
