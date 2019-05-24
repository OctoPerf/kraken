package com.kraken.commons.storage.service;

import com.google.common.testing.NullPointerTester;
import com.kraken.commons.rest.configuration.ApplicationProperties;
import org.junit.Test;

import java.nio.file.Path;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

public class FileSystemStorageWatcherServiceTest {

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester()
        .setDefault(ApplicationProperties.class, ApplicationProperties.DEFAULT)
        .testConstructors(FileSystemStorageWatcherService.class, PACKAGE);
  }

}
