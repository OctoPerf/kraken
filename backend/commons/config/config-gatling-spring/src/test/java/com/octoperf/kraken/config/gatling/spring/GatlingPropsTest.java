package com.octoperf.kraken.config.gatling.spring;

import com.octoperf.kraken.tests.utils.TestUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.Assert.assertNotNull;

class GatlingPropsTest {

  private static final GatlingProps GATLING_PROPERTIES = GatlingProps.builder().build();

  @Test
  void shouldPassToString() {
    TestUtils.shouldPassToString(GATLING_PROPERTIES);
  }

  @Test
  void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(GatlingProps.class).verify();
  }

  @Test
  void shouldCreate() {
    assertNotNull(GATLING_PROPERTIES);
  }
}
