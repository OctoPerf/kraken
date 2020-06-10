package com.octoperf.kraken.security.admin.client.keycloak;

import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
import com.octoperf.kraken.security.admin.client.api.SecurityAdminClient;
import com.octoperf.kraken.security.entity.user.KrakenUser;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class KeycloakSecurityAdminClient implements SecurityAdminClient {

  WebClient webClient;
  SecurityClientProperties properties;

  @Override
  public Mono<KrakenUser> getUser(final String userId) {
    return retry(webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path(getUserUrl(userId)).build())
        .retrieve()
        .bodyToMono(KrakenUser.class), log);
  }

  @Override
  public Mono<KrakenUser> setUser(final KrakenUser user) {
    return retry(webClient
        .put()
        .uri(uriBuilder -> uriBuilder.path(getUserUrl(user.getId())).build())
        .body(BodyInserters.fromValue(user))
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty(""), log)
        .doOnSubscribe(subscription -> log.info("Updating user "+ user.getEmail()))
        .doOnNext(log::info)
        .doOnNext(str -> log.info("Updated user attributes "+ user.getAttributes().toString()))
        .map(str -> user);
  }

  private String getUserUrl(final String userId) {
    return String.format("/auth/admin/realms/%s/users/%s", this.properties.getRealm(), userId);
  }
}
