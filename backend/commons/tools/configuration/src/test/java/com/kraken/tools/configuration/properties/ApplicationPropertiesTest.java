package com.kraken.tools.configuration.properties;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

import java.nio.file.Paths;

public class ApplicationPropertiesTest {

  public static final ApplicationProperties APPLICATION_PROPERTIES = ApplicationProperties.builder()
      .data(Paths.get("testDir"))
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(APPLICATION_PROPERTIES);
  }
}
