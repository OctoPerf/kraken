package com.kraken.config.security.container.spring;

import com.kraken.config.security.container.api.SecurityContainerProperties;
import com.kraken.tests.utils.TestUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SecurityContainerPropertiesTest {
  public static final SecurityContainerProperties SECURITY_PROPERTIES = SpringSecurityContainerProperties.builder()
      .accessToken("accessToken")
      .refreshToken("refreshToken")
      .minValidity(60L)
      .refreshMinValidity(300L)
      .expiresIn(300L)
      .refreshExpiresIn(1800L)
      .build();

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(SECURITY_PROPERTIES);
  }

  @Test
  public void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(SpringSecurityContainerProperties.class).verify();
  }

  @Test
  public void shouldCreate() {
    assertNotNull(SECURITY_PROPERTIES);
  }
}
