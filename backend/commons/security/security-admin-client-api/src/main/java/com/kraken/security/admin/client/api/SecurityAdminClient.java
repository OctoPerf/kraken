package com.kraken.security.admin.client.api;

import com.kraken.security.authentication.client.api.AuthenticatedClient;
import com.kraken.security.entity.user.KrakenUser;
import reactor.core.publisher.Mono;

public interface SecurityAdminClient extends AuthenticatedClient {

  Mono<KrakenUser> getUser(String userId);

  Mono<KrakenUser> setUser(KrakenUser user);
}
