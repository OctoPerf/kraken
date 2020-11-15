package com.octoperf.kraken.security.authentication.client.spring;

import com.octoperf.kraken.config.api.UrlProperty;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.api.ExchangeFilter;
import com.octoperf.kraken.security.authentication.api.ExchangeFilterFactory;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClient;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@AllArgsConstructor
public abstract class WebAuthenticatedClientBuilder<T extends AuthenticatedClient, P extends UrlProperty> implements AuthenticatedClientBuilder<T> {

  @NonNull final List<ExchangeFilterFactory> exchangeFilterFactories;
  @NonNull final P properties;

  protected WebClient.Builder getWebClientBuilder(final AuthenticatedClientBuildOrder order) {
    final var builder = WebClient.builder().baseUrl(properties.getUrl());
    builder.filter(this.getExchangeFilter(order.getMode(), order.getUserId()));
    builder.defaultHeader("ProjectId", order.getProjectId());
    builder.defaultHeader("ApplicationId", order.getApplicationId());
    return builder;
  }

  protected ExchangeFilter getExchangeFilter(final AuthenticationMode mode, final String userId) {
    return exchangeFilterFactories.stream()
        .filter(exchangeFilter -> exchangeFilter.getMode().equals(mode))
        .findFirst()
        .orElseThrow()
        .create(userId);
  }
}
