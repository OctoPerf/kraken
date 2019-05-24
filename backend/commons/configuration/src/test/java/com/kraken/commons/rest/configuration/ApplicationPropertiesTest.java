package com.kraken.commons.rest.configuration;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

import java.nio.file.Paths;

public class ApplicationPropertiesTest {

  public static final ApplicationProperties APPLICATION_PROPERTIES = ApplicationProperties.builder()
      .data(Paths.get("."))
      .hostData("hostData")
      .hostUId("1001")
      .hostGId("1001")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(APPLICATION_PROPERTIES);
  }
}
