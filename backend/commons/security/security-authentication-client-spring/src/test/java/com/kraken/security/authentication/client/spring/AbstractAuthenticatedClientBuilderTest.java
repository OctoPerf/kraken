package com.kraken.security.authentication.client.spring;

import com.google.common.collect.ImmutableList;
import com.kraken.config.api.UrlProperty;
import com.kraken.security.authentication.api.ExchangeFilter;
import com.kraken.security.authentication.api.ExchangeFilterFactory;
import com.kraken.security.authentication.client.api.AuthenticatedClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.kraken.security.authentication.api.AuthenticationMode.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AbstractAuthenticatedClientBuilderTest {

  private static class TestAuthenticatedClient implements AuthenticatedClient {
    public final WebClient webClient;

    public TestAuthenticatedClient(final WebClient webClient) {
      this.webClient = webClient;
    }
  }

  private static class TestAuthenticatedClientBuilder extends AbstractAuthenticatedClientBuilder<TestAuthenticatedClient, UrlProperty> {

    public TestAuthenticatedClientBuilder(final List<ExchangeFilterFactory> exchangeFilterFactories,
                                          final UrlProperty property) {
      super(exchangeFilterFactories, property);
    }

    @Override
    public TestAuthenticatedClient build() {
      return new TestAuthenticatedClient(webClientBuilder.build());
    }
  }

  @Mock
  UrlProperty property;
  @Mock
  ExchangeFilterFactory noopFilterFactory;
  @Mock
  ExchangeFilter noopExchangeFilter;
  @Mock
  ExchangeFilterFactory webFilterFactory;
  @Mock
  ExchangeFilter webExchangeFilter;

  TestAuthenticatedClientBuilder factory;

  @Before
  public void setUp() {
    given(noopFilterFactory.getMode()).willReturn(NOOP);
    given(noopFilterFactory.create(Mockito.anyString())).willReturn(noopExchangeFilter);
    given(webFilterFactory.getMode()).willReturn(SESSION);
    given(webFilterFactory.create(Mockito.anyString())).willReturn(webExchangeFilter);
    given(property.getUrl()).willReturn("url");
    factory = new TestAuthenticatedClientBuilder(ImmutableList.of(noopFilterFactory, webFilterFactory), property);
  }

  @Test
  public void shouldCreateNoop() {
    factory.mode(NOOP).build();
    verify(noopFilterFactory).create("");
  }

  @Test
  public void shouldCreateWeb() {
    factory.mode(SESSION).build();
    verify(webFilterFactory).create("");
  }

  @Test
  public void shouldCreateUserId() {
    factory.mode(SESSION, "userId").build();
    verify(webFilterFactory).create("userId");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldCreateImpersonateFail() {
    factory.mode(IMPERSONATE);
  }
}