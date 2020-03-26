package com.kraken.gatling.properties.spring;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class GatlingPropertiesTest {

  public static final ImmutableGatlingProperties GATLING_PROPERTIES = ImmutableGatlingProperties.builder()
      .home("gatlingHome")
      .debugLog("debugLog")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(GATLING_PROPERTIES);
  }
}
