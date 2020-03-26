package com.kraken.analysis.client.properties;

import com.kraken.analysis.client.properties.api.AnalysisClientProperties;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class ImmutableAnalysisClientPropertiesTest {

  public static final AnalysisClientProperties ANALYSIS_CLIENT_PROPERTIES = ImmutableAnalysisClientProperties.builder()
      .url("analysisUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(ANALYSIS_CLIENT_PROPERTIES);
  }


}
