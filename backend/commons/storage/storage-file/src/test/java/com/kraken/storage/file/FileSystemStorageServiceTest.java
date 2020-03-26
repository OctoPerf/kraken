package com.kraken.storage.file;

import com.google.common.testing.NullPointerTester;
import com.kraken.tools.properties.ApplicationPropertiesTest;
import com.kraken.tools.properties.ImmutableApplicationProperties;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

public class FileSystemStorageServiceTest {

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester()
        .setDefault(ImmutableApplicationProperties.class, ApplicationPropertiesTest.APPLICATION_PROPERTIES)
        .testConstructors(FileSystemStorageService.class, PACKAGE);
  }

}
