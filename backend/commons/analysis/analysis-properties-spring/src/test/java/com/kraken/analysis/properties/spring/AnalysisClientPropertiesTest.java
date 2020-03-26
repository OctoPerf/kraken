package com.kraken.analysis.properties.spring;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class AnalysisClientPropertiesTest {

  public static final ImmutableAnalysisClientProperties ANALYSIS_CLIENT_PROPERTIES = ImmutableAnalysisClientProperties.builder()
      .url("analysisUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    ANALYSIS_CLIENT_PROPERTIES.log();
    TestUtils.shouldPassAll(ANALYSIS_CLIENT_PROPERTIES);
  }

}
