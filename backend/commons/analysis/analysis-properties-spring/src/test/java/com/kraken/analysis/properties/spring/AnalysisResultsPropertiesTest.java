package com.kraken.analysis.properties.spring;

import org.junit.Test;

import java.nio.file.Paths;

import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static org.assertj.core.api.Assertions.assertThat;

public class AnalysisResultsPropertiesTest {

  public static final ImmutableAnalysisResultsProperties ANALYSIS_PROPERTIES = ImmutableAnalysisResultsProperties.builder()
      .root("resultsRoot")
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(ANALYSIS_PROPERTIES);
  }

  @Test
  public void shouldReturn() {
    ANALYSIS_PROPERTIES.log();
    assertThat(ANALYSIS_PROPERTIES.getResultPath("testId")).isEqualTo(Paths.get("resultsRoot", "testId"));
  }

}
