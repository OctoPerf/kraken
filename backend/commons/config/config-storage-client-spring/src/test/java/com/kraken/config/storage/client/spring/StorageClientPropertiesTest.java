package com.kraken.config.storage.client.spring;

import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

public class StorageClientPropertiesTest {

  public static final SpringStorageClientProperties STORAGE_PROPERTIES = SpringStorageClientProperties.builder()
      .url("storageUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(STORAGE_PROPERTIES);
  }

}
