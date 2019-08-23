package com.kraken.commons.analysis.server;

import com.google.common.testing.NullPointerTester;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.kraken.test.utils.TestUtils.shouldPassEquals;
import static com.kraken.test.utils.TestUtils.shouldPassToString;

public class AnalysisPropertiesTest {

  public static final AnalysisProperties ANALYSIS_PROPERTIES = AnalysisProperties.builder()
      .runProperties(
          RunProperties.builder()
              .root("runnerRoot")
              .script("runnerScript")
              .cancelScript("runnerCancelScript")
              .build()
      )
      .debugProperties(
          RunProperties.builder()
              .root("debuggerRoot")
              .script("debuggerScript")
              .cancelScript("debuggerCancelScript")
              .build()
      )
      .recordProperties(
          RunProperties.builder()
              .root("recorderRoot")
              .script("recorderScript")
              .cancelScript("")
              .build()
      )
      .analysisUrl("analysisUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassEquals(ANALYSIS_PROPERTIES.getClass());
    new NullPointerTester().setDefault(RunProperties.class, RunPropertiesTest.RUN_PROPERTIES).testConstructors(ANALYSIS_PROPERTIES.getClass(), PACKAGE);
    shouldPassToString(ANALYSIS_PROPERTIES);
  }


}
