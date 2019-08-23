package com.kraken.analysis.properties;

import com.kraken.test.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.nio.file.Paths;

public class AnalysisPropertiesTest {

  public static final AnalysisProperties ANALYSIS_PROPERTIES = AnalysisProperties.builder()
      .resultsRoot("resultsRoot")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(ANALYSIS_PROPERTIES);
  }

  @Test
  public void shouldReturn() {
    Assertions.assertThat(ANALYSIS_PROPERTIES.getResultPath("testId")).isEqualTo(Paths.get("resultsRoot", "testId"));
  }

}
