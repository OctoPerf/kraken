package com.octoperf.kraken.config.cors.spring;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.config.cors.api.CorsProperties;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

import java.time.Duration;

class CorsPropertiesTest {

  public static final CorsProperties CORS_PROPERTIES = SpringCorsProperties
      .builder()
      .allowedOrigins(ImmutableList.of("*"))
      .allowedHeaders(ImmutableList.of("*"))
      .allowedMethods(ImmutableList.of("*"))
      .allowCredentials(true)
      .maxAge(Duration.ofHours(1))
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(CORS_PROPERTIES);
  }
}