package com.octoperf.kraken.security.authentication.service.account;

import com.octoperf.kraken.security.authentication.utils.DefaultExchangeFilter;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.SERVICE_ACCOUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ServiceAccountExchangeFilterFactoryTest {
  @Mock
  ServiceAccountUserProviderFactory userProviderFactory;
  @Mock
  ServiceAccountUserProvider userProvider;

  ServiceAccountExchangeFilterFactory exchangeFilterFactory;

  @BeforeEach
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