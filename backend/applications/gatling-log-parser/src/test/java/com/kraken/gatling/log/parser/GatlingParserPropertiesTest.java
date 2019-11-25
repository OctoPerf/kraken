package com.kraken.gatling.log.parser;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

import java.nio.file.Path;

public class GatlingParserPropertiesTest {

  public static final GatlingParserProperties  GATLING_PROPERTIES = GatlingParserProperties.builder()
      .gatlingHome(Path.of("gatlingHome"))
      .debugLog(Path.of("debugLog"))
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(GATLING_PROPERTIES);
  }
}
