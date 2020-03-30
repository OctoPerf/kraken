package com.kraken.storage.file;

import com.google.common.testing.NullPointerTester;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

public class FileSystemStorageWatcherServiceTest {

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester()
        .testConstructors(FileSystemStorageWatcherService.class, PACKAGE);
  }

}
