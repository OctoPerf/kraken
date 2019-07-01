package com.kraken.commons.storage.client;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class StorageClientPropertiesTest {

  public static final StorageClientProperties ANALYSIS_PROPERTIES = StorageClientProperties.builder()
      .storageUrl("storageUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(ANALYSIS_PROPERTIES);
  }

}
