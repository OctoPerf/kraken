package com.kraken.security.authentication.session;

import com.kraken.security.authentication.utils.DefaultExchangeFilter;
import com.kraken.tests.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.kraken.security.authentication.api.AuthenticationMode.SESSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SessionExchangeFilterFactoryTest {
  @Mock
  SessionUserProviderFactory userProviderFactory;
  @Mock
  SessionUserProvider userProvider;

  SessionExchangeFilterFactory exchangeFilterFactory;

  @Before
  public void setUp() {
    exchangeFilterFactory = new SessionExchangeFilterFactory(userProviderFactory);
  }

  @Test
  public void shouldCreate() {
    given(userProviderFactory.create("userId")).willReturn(userProvider);
    assertThat(exchangeFilterFactory.create("userId")).isInstanceOf(DefaultExchangeFilter.class);
  }

  @Test
  public void shouldGetMode() {
    assertThat(exchangeFilterFactory.getMode()).isEqualTo(SESSION);
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(SessionExchangeFilterFactory.class);
  }
}