package com.octoperf.kraken.config.kraken;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class ApplicationPropertiesTest {

  public static final SpringApplicationProperties APPLICATION_PROPERTIES = SpringApplicationProperties.builder()
    .data("testDir")
    .version("2.0.0")
    .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassEquals(APPLICATION_PROPERTIES.getClass());
    TestUtils.shouldPassToString(APPLICATION_PROPERTIES);
  }
}
