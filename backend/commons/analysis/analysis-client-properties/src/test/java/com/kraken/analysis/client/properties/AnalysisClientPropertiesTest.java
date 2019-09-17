package com.kraken.analysis.client.properties;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class AnalysisClientPropertiesTest {

  public static final AnalysisClientProperties ANALYSIS_CLIENT_PROPERTIES = AnalysisClientProperties.builder()
      .analysisUrl("analysisUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(ANALYSIS_CLIENT_PROPERTIES);
  }


}
