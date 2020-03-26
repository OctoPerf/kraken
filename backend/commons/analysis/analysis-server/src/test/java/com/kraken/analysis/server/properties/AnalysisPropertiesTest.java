package com.kraken.analysis.server.properties;

import com.kraken.test.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.nio.file.Paths;

public class AnalysisPropertiesTest {

  public static final ImmutableAnalysisProperties ANALYSIS_PROPERTIES = ImmutableAnalysisProperties.builder()
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
