package com.kraken.security.configuration;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.junit.Assert.*;

public class JwtConfigurationTest {

  @Test
  public void shouldReturnSecurityContext() {
    Assertions.assertThat(new JwtConfiguration().securityContext()).isNotNull();
  }
}