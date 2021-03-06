package com.octoperf.kraken.security.authentication.container;

import com.octoperf.kraken.security.authentication.utils.DefaultExchangeFilter;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.CONTAINER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ContainerExchangeFilterFactoryTest {
  @Mock
  ContainerUserProviderFactory userProviderFactory;
  @Mock
  ContainerUserProvider userProvider;

  ContainerExchangeFilterFactory exchangeFilterFactory;

  @BeforeEach
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