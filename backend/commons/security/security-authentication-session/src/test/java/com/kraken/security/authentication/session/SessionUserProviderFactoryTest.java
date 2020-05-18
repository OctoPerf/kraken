package com.kraken.security.authentication.session;

import com.kraken.tests.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.kraken.security.authentication.api.AuthenticationMode.SESSION;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SessionUserProviderFactoryTest {

  @Mock
  SessionUserProvider provider;

  SessionUserProviderFactory factory;

  @Before
  public void setUp() {
    factory = new SessionUserProviderFactory(provider);
  }

  @Test
  public void shouldCreate() {
    final var userProvider = factory.create("userId");
    assertThat(userProvider).isSameAs(provider);
  }

  @Test
  public void shouldGetMode() {
    assertThat(factory.getMode()).isEqualTo(SESSION);
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(SessionUserProviderFactory.class);
  }

}