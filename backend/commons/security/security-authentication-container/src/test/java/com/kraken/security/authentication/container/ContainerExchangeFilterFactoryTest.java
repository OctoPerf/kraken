package com.kraken.security.authentication.container;

import com.kraken.security.authentication.utils.DefaultExchangeFilter;
import com.kraken.tests.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.kraken.security.authentication.api.AuthenticationMode.CONTAINER;
import static com.kraken.security.authentication.api.AuthenticationMode.SERVICE_ACCOUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ContainerExchangeFilterFactoryTest {
  @Mock
  ContainerUserProviderFactory userProviderFactory;
  @Mock
  ContainerUserProvider userProvider;

  ContainerExchangeFilterFactory exchangeFilterFactory;

  @Before
  public void setUp() {
    exchangeFilterFactory = new ContainerExchangeFilterFactory(userProviderFactory);
  }

  @Test
  public void shouldCreate() {
    given(userProviderFactory.create("userId")).willReturn(userProvider);
    assertThat(exchangeFilterFactory.create("userId")).isInstanceOf(DefaultExchangeFilter.class);
  }

  @Test
  public void shouldGetMode() {
    assertThat(exchangeFilterFactory.getMode()).isEqualTo(CONTAINER);
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(ContainerExchangeFilterFactory.class);
  }
}