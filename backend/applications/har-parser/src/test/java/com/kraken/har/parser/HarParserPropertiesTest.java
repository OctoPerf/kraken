package com.kraken.har.parser;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassEquals;
import static com.kraken.test.utils.TestUtils.shouldPassToString;

public class HarParserPropertiesTest {

  public static final ImmutableHarParserProperties HAR_PROPERTIES = ImmutableHarParserProperties.builder()
      .local("localHarPath")
      .remote("remoteHarPath")
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassToString(HAR_PROPERTIES);
    shouldPassEquals(HAR_PROPERTIES.getClass());
  }
}
