package com.kraken.commons.storage.service;

import com.google.common.testing.NullPointerTester;
import com.kraken.commons.rest.configuration.ApplicationProperties;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

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
