package com.kraken.tools.gatling.properties;

import com.kraken.test.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.nio.file.Paths;

public class GatlingPropertiesTest {

  public static final GatlingProperties GATLING_PROPERTIES = GatlingProperties.builder()
      .resultsRoot("resultsRoot")
      .version("version")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(GATLING_PROPERTIES);
  }

  @Test
  public void shouldReturn() {
    Assertions.assertThat(GATLING_PROPERTIES.getTestResultPath("testId")).isEqualTo(Paths.get("resultsRoot", "testId"));
  }

}
