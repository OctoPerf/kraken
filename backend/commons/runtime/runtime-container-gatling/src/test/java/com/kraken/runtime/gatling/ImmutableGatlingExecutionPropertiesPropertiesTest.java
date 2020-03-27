package com.kraken.runtime.gatling;

import com.kraken.runtime.gatling.api.GatlingExecutionProperties;
import com.kraken.test.utils.TestUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ImmutableGatlingExecutionPropertiesPropertiesTest {

  private static final GatlingExecutionProps GATLING_PROPERTIES = new GatlingExecutionProps();

  static {
    GATLING_PROPERTIES.setHome("gatlingHome");
    GATLING_PROPERTIES.setBin("gatlingBin");
    GATLING_PROPERTIES.setJavaOpts("-Dfoo=\"bar\"");
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassToString(GATLING_PROPERTIES);
  }

  @Test
  public void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(GatlingExecutionProperties.class).verify();
  }

  @Test
  public void shouldCreate() {
    GATLING_PROPERTIES.log();
    assertNotNull(GATLING_PROPERTIES);
  }
}
