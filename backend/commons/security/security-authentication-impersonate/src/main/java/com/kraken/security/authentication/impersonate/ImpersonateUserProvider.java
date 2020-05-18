package com.kraken.security.authentication.impersonate;

import com.kraken.config.security.client.api.SecurityClientProperties;
import com.kraken.security.authentication.utils.AtomicUserProvider;
import com.kraken.security.client.api.SecurityClient;
import com.kraken.security.decoder.api.TokenDecoder;
import com.kraken.security.entity.token.KrakenToken;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ImpersonateUserProvider extends AtomicUserProvider {

  SecurityClientProperties clientProperties;
  SecurityClient client;
  String userId;

  public ImpersonateUserProvider(final SecurityClientProperties clientProperties,
                                 final TokenDecoder decoder,
                                 final SecurityClient client,
                                 final String userId) {
    super(decoder, 60L);
    this.clientProperties = requireNonNull(clientProperties);
    this.client = requireNonNull(client);
    this.userId = requireNonNull(userId);;
  }

  @Override
  protected Mono<KrakenToken> newToken() {
    return client.impersonate(clientProperties.getApi(), userId);
  }

  @Override
  protected Mono<KrakenToken> refreshToken(KrakenToken token) {
    return client.refreshToken(clientProperties.getApi(), token.getRefreshToken());
  }

}
