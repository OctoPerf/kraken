package com.kraken.config.storage.spring;

import com.kraken.config.storage.spring.SpringStorageProperties;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class StoragePropertiesTest {

  public static final SpringStorageProperties STORAGE_PROPERTIES = SpringStorageProperties.builder()
      .url("storageUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(STORAGE_PROPERTIES);
  }

}
