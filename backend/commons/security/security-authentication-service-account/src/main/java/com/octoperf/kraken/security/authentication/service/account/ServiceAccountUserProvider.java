package com.octoperf.kraken.security.authentication.service.account;

import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
import com.octoperf.kraken.security.authentication.utils.AtomicUserProvider;
import com.octoperf.kraken.security.client.api.SecurityClient;
import com.octoperf.kraken.security.decoder.api.TokenDecoder;
import com.octoperf.kraken.security.entity.token.KrakenToken;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ServiceAccountUserProvider extends AtomicUserProvider {

  SecurityClientProperties clientProperties;
  Mono<SecurityClient> clientMono;

  public ServiceAccountUserProvider(@NonNull final SecurityClientProperties clientProperties,
                                    final TokenDecoder decoder,
                                    @NonNull final Mono<SecurityClient> clientMono) {
    super(decoder, 60L);
    this.clientProperties = clientProperties;
    this.clientMono = clientMono;
  }

  @Override
  protected Mono<KrakenToken> newToken() {
    return clientMono.flatMap(client -> client.clientLogin(clientProperties.getApi()));
  }

  @Override
  protected Mono<KrakenToken> refreshToken(KrakenToken token) {
    return this.newToken();
  }

}
