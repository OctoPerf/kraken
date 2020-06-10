package com.octoperf.kraken.config.analysis.client.spring;

import com.octoperf.kraken.config.analysis.client.api.AnalysisClientProperties;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class AnalysisClientPropertiesTest {

  public static final AnalysisClientProperties ANALYSIS_CLIENT_PROPERTIES = SpringAnalysisClientProperties.builder()
      .url("analysisUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(ANALYSIS_CLIENT_PROPERTIES);
  }

}
