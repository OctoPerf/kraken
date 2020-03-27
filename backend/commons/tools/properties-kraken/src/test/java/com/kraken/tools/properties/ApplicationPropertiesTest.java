package com.kraken.tools.properties;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class ApplicationPropertiesTest {

  public static final SpringApplicationProperties APPLICATION_PROPERTIES = SpringApplicationProperties.builder()
    .data("testDir")
    .version("2.0.0")
    .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(APPLICATION_PROPERTIES);
  }
}
