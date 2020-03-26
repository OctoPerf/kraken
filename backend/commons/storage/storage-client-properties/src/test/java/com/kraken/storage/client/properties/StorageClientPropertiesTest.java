package com.kraken.storage.client.properties;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class StorageClientPropertiesTest {

  public static final ImmutableStorageClientProperties STORAGE_PROPERTIES = ImmutableStorageClientProperties.builder()
      .url("storageUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    STORAGE_PROPERTIES.log();
    TestUtils.shouldPassAll(STORAGE_PROPERTIES);
  }

}
