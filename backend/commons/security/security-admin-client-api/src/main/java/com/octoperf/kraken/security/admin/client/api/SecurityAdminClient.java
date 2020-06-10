package com.octoperf.kraken.security.admin.client.api;

import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClient;
import com.octoperf.kraken.security.entity.user.KrakenUser;
import reactor.core.publisher.Mono;

public interface SecurityAdminClient extends AuthenticatedClient {

  Mono<KrakenUser> getUser(String userId);

  Mono<KrakenUser> setUser(KrakenUser user);
}
