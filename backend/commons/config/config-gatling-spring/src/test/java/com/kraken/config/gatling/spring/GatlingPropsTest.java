package com.kraken.config.gatling.spring;

import com.kraken.tests.utils.TestUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GatlingPropsTest {

  private static final GatlingProps GATLING_PROPERTIES = GatlingProps.builder().build();

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(GATLING_PROPERTIES);
  }

  @Test
  public void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(GatlingProps.class).verify();
  }

  @Test
  public void shouldCreate() {
    assertNotNull(GATLING_PROPERTIES);
  }
}
