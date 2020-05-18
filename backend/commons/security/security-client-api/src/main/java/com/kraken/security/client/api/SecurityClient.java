package com.kraken.security.client.api;

import com.kraken.config.security.client.api.SecurityClientCredentialsProperties;
import com.kraken.security.entity.token.KrakenToken;
import com.kraken.tools.webclient.Client;
import reactor.core.publisher.Mono;

public interface SecurityClient extends Client {

  Mono<KrakenToken> userLogin(SecurityClientCredentialsProperties client,
                              String username,
                              String password);

  Mono<KrakenToken> clientLogin(SecurityClientCredentialsProperties client);

  Mono<KrakenToken> exchangeToken(SecurityClientCredentialsProperties client,
                                  String accessToken);

  Mono<KrakenToken> refreshToken(SecurityClientCredentialsProperties client,
                                 String refreshToken);

  Mono<KrakenToken> impersonate(SecurityClientCredentialsProperties client,
                                String userId);
}
