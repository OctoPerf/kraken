package com.kraken.security.authentication.impersonate;

import com.kraken.security.authentication.utils.DefaultExchangeFilter;
import com.kraken.tests.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.kraken.security.authentication.api.AuthenticationMode.IMPERSONATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ImpersonateExchangeFilterFactoryTest {
  @Mock
  ImpersonateUserProviderFactory userProviderFactory;
  @Mock
  ImpersonateUserProvider userProvider;

  ImpersonateExchangeFilterFactory exchangeFilterFactory;

  @Before
  public void setUp() {
    exchangeFilterFactory = new ImpersonateExchangeFilterFactory(userProviderFactory);
  }

  @Test
  public void shouldCreate() {
    given(userProviderFactory.create("userId")).willReturn(userProvider);
    assertThat(exchangeFilterFactory.create("userId")).isInstanceOf(DefaultExchangeFilter.class);
  }

  @Test
  public void shouldGetMode() {
    assertThat(exchangeFilterFactory.getMode()).isEqualTo(IMPERSONATE);
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(ImpersonateExchangeFilterFactory.class);
  }
}