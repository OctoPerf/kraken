package com.kraken.storage.client.properties;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class StoragePropertiesTest {

  public static final SpringStorageProperties STORAGE_PROPERTIES = SpringStorageProperties.builder()
      .url("storageUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    STORAGE_PROPERTIES.log();
    TestUtils.shouldPassAll(STORAGE_PROPERTIES);
  }

}
