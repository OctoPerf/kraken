package com.kraken.security.authentication.noop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NoopUserProviderTest {

  NoopUserProvider userProvider;

  @BeforeEach
  public void setUp() {
    userProvider = new NoopUserProvider();
  }

  @Test
  public void shouldReturnToken() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      userProvider.getTokenValue().block();
    });
  }

  @Test
  public void shouldReturnUser() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      userProvider.getAuthenticatedUser().block();
    });
  }

}