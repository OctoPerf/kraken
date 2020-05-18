package com.kraken.config.security.client.spring;

import com.kraken.config.security.client.api.SecurityClientProperties;
import com.kraken.tests.utils.TestUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SecurityClientPropertiesTest {

  public static final SecurityClientProperties SECURITY_CLIENT_PROPERTIES = SpringSecurityClientProperties.builder()
      .url("url")
      .realm("realm")
      .web(new SpringSecurityClientCredentialsProperties("kraken-web", ""))
      .api(new SpringSecurityClientCredentialsProperties("kraken-api", "secret"))
      .container(new SpringSecurityClientCredentialsProperties("kraken-container", "secret"))
      .build();

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(SECURITY_CLIENT_PROPERTIES);
  }

  @Test
  public void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(SpringSecurityClientProperties.class).verify();
  }

  @Test
  public void shouldCreate() {
    assertNotNull(SECURITY_CLIENT_PROPERTIES);
  }
}
