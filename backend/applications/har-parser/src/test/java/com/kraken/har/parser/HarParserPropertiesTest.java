package com.kraken.har.parser;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

import java.nio.file.Path;

public class HarParserPropertiesTest {

  public static final HarParserProperties HAR_PROPERTIES = HarParserProperties.builder()
      .localHarPath(Path.of("localHarPath"))
      .remoteHarPath("remoteHarPath")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(HAR_PROPERTIES);
  }
}
