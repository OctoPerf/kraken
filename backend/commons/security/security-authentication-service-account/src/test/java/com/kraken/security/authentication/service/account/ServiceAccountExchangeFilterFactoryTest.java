package com.kraken.security.authentication.service.account;

import com.kraken.security.authentication.api.UserProvider;
import com.kraken.security.authentication.utils.DefaultExchangeFilter;
import com.kraken.tests.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.kraken.security.authentication.api.AuthenticationMode.SERVICE_ACCOUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ServiceAccountExchangeFilterFactoryTest {
  @Mock
  ServiceAccountUserProviderFactory userProviderFactory;
  @Mock
  ServiceAccountUserProvider userProvider;

  ServiceAccountExchangeFilterFactory exchangeFilterFactory;

  @Before
  public void setUp() {
    exchangeFilterFactory = new ServiceAccountExchangeFilterFactory(userProviderFactory);
  }

  @Test
  public void shouldCreate() {
    given(userProviderFactory.create("userId")).willReturn(userProvider);
    assertThat(exchangeFilterFactory.create("userId")).isInstanceOf(DefaultExchangeFilter.class);
  }

  @Test
  public void shouldGetMode() {
    assertThat(exchangeFilterFactory.getMode()).isEqualTo(SERVICE_ACCOUNT);
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(ServiceAccountExchangeFilterFactory.class);
  }
}