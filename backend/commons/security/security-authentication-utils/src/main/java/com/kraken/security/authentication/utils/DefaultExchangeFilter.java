package com.kraken.security.authentication.utils;

import com.kraken.security.authentication.api.ExchangeFilter;
import com.kraken.security.authentication.api.UserProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class DefaultExchangeFilter implements ExchangeFilter {

  @NonNull UserProvider userProvider;

  @Override
  public Mono<ClientResponse> filter(final ClientRequest request, final ExchangeFunction next) {
    return userProvider.getTokenValue().map(token -> ClientRequest.from(request)
        .headers(headers -> headers.setBearerAuth(token))
        .build())
        .flatMap(next::exchange);
  }
}
