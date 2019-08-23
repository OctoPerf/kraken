package com.kraken.storage.file;

import com.google.common.testing.NullPointerTester;
import com.kraken.tools.configuration.properties.ApplicationProperties;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

public class FileSystemStorageServiceTest {

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester()
        .setDefault(ApplicationProperties.class, ApplicationProperties.DEFAULT)
        .testConstructors(FileSystemStorageService.class, PACKAGE);
  }

}
