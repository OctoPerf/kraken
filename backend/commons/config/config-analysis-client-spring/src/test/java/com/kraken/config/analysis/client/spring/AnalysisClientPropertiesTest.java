package com.kraken.config.analysis.client.spring;

import com.kraken.config.analysis.client.api.AnalysisClientProperties;
import com.kraken.tests.utils.TestUtils;
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
