package com.kraken.commons.storage.synchronizer.properties;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class StorageSynchronizerPropertiesTest {

  public static final StorageSynchronizerProperties STORAGE_SYNCHRONIZER_PROPERTIES = StorageSynchronizerProperties.builder()
      .updateFilter("updateFilter")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(STORAGE_SYNCHRONIZER_PROPERTIES);
  }

}
