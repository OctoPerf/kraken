package com.kraken.config.har.parser.spring;

import org.junit.Test;

import static com.kraken.tests.utils.TestUtils.shouldPassEquals;
import static com.kraken.tests.utils.TestUtils.shouldPassToString;

public class HarParserPropertiesTest {

  public static final SpringHarParserProperties HAR_PROPERTIES = SpringHarParserProperties.builder()
      .local("localHarPath")
      .remote("remoteHarPath")
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassToString(HAR_PROPERTIES);
    shouldPassEquals(HAR_PROPERTIES.getClass());
  }
}
