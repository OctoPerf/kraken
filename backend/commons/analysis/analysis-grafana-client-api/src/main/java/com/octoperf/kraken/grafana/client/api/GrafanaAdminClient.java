package com.octoperf.kraken.grafana.client.api;

import com.octoperf.kraken.tools.webclient.Client;
import reactor.core.publisher.Mono;

public interface GrafanaAdminClient extends Client {

  Mono<GrafanaUser> createUser(String userId, String email);

  Mono<String> deleteUser(String userId);

  Mono<GrafanaUser> updateUser(GrafanaUser user, String userId, String email);
}
