package com.octoperf.kraken.config.storage.client.spring;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class StorageClientPropertiesTest {

  public static final SpringStorageClientProperties STORAGE_PROPERTIES = SpringStorageClientProperties.builder()
      .url("storageUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(STORAGE_PROPERTIES);
  }

}
