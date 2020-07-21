package com.octoperf.kraken.config.analysis.spring;

import com.octoperf.kraken.config.grafana.api.AnalysisResultsProperties;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.octoperf.kraken.tests.utils.TestUtils.shouldPassAll;
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
