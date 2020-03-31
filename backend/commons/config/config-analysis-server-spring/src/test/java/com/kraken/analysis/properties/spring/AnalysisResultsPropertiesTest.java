package com.kraken.analysis.properties.spring;

import com.kraken.config.grafana.api.AnalysisResultsProperties;
import com.kraken.config.analysis.spring.SpringAnalysisResultsProperties;
import org.junit.Test;

import java.nio.file.Paths;

import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static org.assertj.core.api.Assertions.assertThat;

public class AnalysisResultsPropertiesTest {

  public static final AnalysisResultsProperties ANALYSIS_PROPERTIES = SpringAnalysisResultsProperties.builder()
      .root("resultsRoot")
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(ANALYSIS_PROPERTIES);
  }

  @Test
  public void shouldReturn() {
    assertThat(ANALYSIS_PROPERTIES.getResultPath("testId")).isEqualTo(Paths.get("resultsRoot", "testId"));
  }

}
