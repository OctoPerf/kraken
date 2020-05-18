package com.kraken.storage.file;

import com.google.common.testing.NullPointerTester;
import io.methvin.watcher.DirectoryChangeEvent;
import org.junit.Test;
import reactor.core.publisher.Flux;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

public class FileSystemStorageServiceTest {

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester()
        .setDefault(Flux.class, Flux.empty())
        .testConstructors(FileSystemStorageService.class, PACKAGE);
  }

}
