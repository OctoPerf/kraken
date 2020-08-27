package com.octoperf.kraken.security.authentication.client.spring;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.config.api.UrlProperty;
import com.octoperf.kraken.security.authentication.api.ExchangeFilter;
import com.octoperf.kraken.security.authentication.api.ExchangeFilterFactory;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClient;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WebAuthenticatedClientBuilderTest {

  private static class TestAuthenticatedClient implements AuthenticatedClient {
    public final WebClient webClient;

    public TestAuthenticatedClient(final WebClient webClient) {
      this.webClient = webClient;
    }
  }

  private static class TestAuthenticatedClientBuilder extends WebAuthenticatedClientBuilder<TestAuthenticatedClient, UrlProperty> {

    public TestAuthenticatedClientBuilder(final List<ExchangeFilterFactory> exchangeFilterFactories,
                                          final UrlProperty property) {
      super(exchangeFilterFactories, property);
    }

    @Override
    public Mono<TestAuthenticatedClient> build(final AuthenticatedClientBuildOrder order) {
      return Mono.just(new TestAuthenticatedClient(getWebClientBuilder(order).build()));
    }
  }

  @Mock(lenient = true)
  UrlProperty property;
  @Mock(lenient = true)
  ExchangeFilterFactory noopFilterFactory;
  @Mock
  ExchangeFilter noopExchangeFilter;
  @Mock(lenient = true)
  ExchangeFilterFactory webFilterFactory;
  @Mock
  ExchangeFilter webExchangeFilter;

  TestAuthenticatedClientBuilder factory;

  @BeforeEach
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
    factory.build(AuthenticatedClientBuildOrder.builder().mode(NOOP).build());
    verify(noopFilterFactory).create("");
  }

  @Test
  public void shouldCreateWeb() {
    factory.build(AuthenticatedClientBuildOrder.builder().mode(SESSION).build());
    verify(webFilterFactory).create("");
  }

  @Test
  public void shouldCreateUserId() {
    factory.build(AuthenticatedClientBuildOrder.builder().mode(SESSION).userId("userId").build());
    verify(webFilterFactory).create("userId");
  }

}