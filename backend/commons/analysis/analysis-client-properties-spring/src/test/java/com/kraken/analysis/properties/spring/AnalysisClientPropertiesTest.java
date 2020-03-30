package com.kraken.analysis.properties.spring;

import com.kraken.analysis.properties.api.AnalysisClientProperties;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class AnalysisClientPropertiesTest {

  public static final AnalysisClientProperties ANALYSIS_CLIENT_PROPERTIES = SpringAnalysisClientProperties.builder()
      .url("analysisUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(ANALYSIS_CLIENT_PROPERTIES);
  }

}
