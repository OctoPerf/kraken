package com.kraken.security.authentication.client.spring;

import com.kraken.config.api.UrlProperty;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.security.authentication.api.ExchangeFilter;
import com.kraken.security.authentication.api.ExchangeFilterFactory;
import com.kraken.security.authentication.client.api.AuthenticatedClient;
import com.kraken.security.authentication.client.api.AuthenticatedClientBuilder;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.kraken.security.authentication.api.AuthenticationMode.IMPERSONATE;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractAuthenticatedClientBuilder<T extends AuthenticatedClient, P extends UrlProperty> implements AuthenticatedClientBuilder<T> {

  final List<ExchangeFilterFactory> exchangeFilterFactories;
  final P properties;
  WebClient.Builder webClientBuilder;

  protected AbstractAuthenticatedClientBuilder(@NonNull final List<ExchangeFilterFactory> exchangeFilterFactories,
                                               @NonNull final P properties) {
    this.exchangeFilterFactories = exchangeFilterFactories;
    this.properties = properties;
    this.webClientBuilder = WebClient.builder().baseUrl(properties.getUrl());
  }

  @Override
  public AuthenticatedClientBuilder<T> mode(AuthenticationMode mode) {
    checkArgument(!mode.equals(IMPERSONATE), "The user id is required for the IMPERSONATE authentication mode");
    return this.mode(mode, "");
  }

  @Override
  public AuthenticatedClientBuilder<T> mode(AuthenticationMode mode, String userId) {
    this.webClientBuilder = this.webClientBuilder.filter(this.getExchangeFilter(mode, userId));
    return this;
  }

  @Override
  public AuthenticatedClientBuilder<T> applicationId(String applicationId) {
    this.webClientBuilder = this.webClientBuilder.defaultHeader("ApplicationId", applicationId);
    return this;
  }

  protected ExchangeFilter getExchangeFilter(final AuthenticationMode mode, String userId) {
    return exchangeFilterFactories.stream()
        .filter(exchangeFilter -> exchangeFilter.getMode().equals(mode))
        .findFirst()
        .orElseThrow()
        .create(userId);
  }
}
