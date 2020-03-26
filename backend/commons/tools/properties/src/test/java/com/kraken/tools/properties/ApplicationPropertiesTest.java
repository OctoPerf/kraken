package com.kraken.tools.properties;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class ApplicationPropertiesTest {

  public static final ImmutableApplicationProperties APPLICATION_PROPERTIES = ImmutableApplicationProperties.builder()
      .data("testDir")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(APPLICATION_PROPERTIES);
  }
}
