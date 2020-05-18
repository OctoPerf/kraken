package com.kraken.security.authentication.noop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.Exceptions;

public class NoopUserProviderTest {

  NoopUserProvider userProvider;

  @Before
  public void setUp() {
    userProvider = new NoopUserProvider();
  }

  @Test(expected = RuntimeException.class)
  public void shouldReturnToken() {
    userProvider.getTokenValue().block();
  }

  @Test(expected = RuntimeException.class)
  public void shouldReturnUser() {
    userProvider.getAuthenticatedUser().block();
  }

}