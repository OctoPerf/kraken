package com.kraken.tools.properties;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class KrakenPropertiesTest {

  public static final ImmutableKrakenProperties APPLICATION_PROPERTIES = ImmutableKrakenProperties.builder()
    .data("testDir")
    .version("2.0.0")
    .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(APPLICATION_PROPERTIES);
  }
}
