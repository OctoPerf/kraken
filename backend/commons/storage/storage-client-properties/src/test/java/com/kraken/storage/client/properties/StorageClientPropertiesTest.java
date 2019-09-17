package com.kraken.storage.client.properties;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class StorageClientPropertiesTest {

  public static final StorageClientProperties STORAGE_PROPERTIES = StorageClientProperties.builder()
      .storageUrl("storageUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(STORAGE_PROPERTIES);
  }

}
