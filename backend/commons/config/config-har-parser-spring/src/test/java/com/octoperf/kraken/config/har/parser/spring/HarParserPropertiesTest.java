package com.octoperf.kraken.config.har.parser.spring;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class HarParserPropertiesTest {

  public static final SpringHarParserProperties HAR_PROPERTIES = SpringHarParserProperties.builder()
      .local("localHarPath")
      .remote("remoteHarPath")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassToString(HAR_PROPERTIES);
    TestUtils.shouldPassEquals(HAR_PROPERTIES.getClass());
  }
}
