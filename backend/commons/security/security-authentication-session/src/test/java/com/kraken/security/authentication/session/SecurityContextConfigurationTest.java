package com.kraken.security.authentication.session;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class SecurityContextConfigurationTest {

  @Test
  public void shouldReturnSecurityContext() {
    Assertions.assertThat(new SecurityContextConfiguration().securityContext()).isNotNull();
  }
}