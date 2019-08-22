package com.kraken.commons.storage.file;

import com.google.common.testing.NullPointerTester;
import com.kraken.commons.rest.configuration.ApplicationProperties;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.FileSystemUtils.deleteRecursively;

public class FileSystemStorageServiceTest {

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester()
        .setDefault(ApplicationProperties.class, ApplicationProperties.DEFAULT)
        .testConstructors(FileSystemStorageService.class, PACKAGE);
  }

}
