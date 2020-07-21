package com.octoperf.kraken.security.client.keycloak;

import com.octoperf.kraken.config.security.client.api.SecurityClientCredentialsProperties;
import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
import com.octoperf.kraken.security.client.api.SecurityClient;
import com.octoperf.kraken.security.entity.token.KrakenToken;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class KeycloakSecurityClient implements SecurityClient {

  @NonNull
  SecurityClientProperties properties;
  @NonNull
  WebClient webClient;

  @Override
  public Mono<KrakenToken> userLogin(final SecurityClientCredentialsProperties client,
                                     final String username,
                                     final String password) {
    return retry(webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path(this.getOpenIdTokenUrl()).build())
        .body(BodyInserters.fromFormData("username", username)
            .with("password", password)
            .with("grant_type", "password")
            .with("client_id", client.getId()))
        .retrieve()
        .bodyToMono(KrakenToken.class), log)
        .doOnSubscribe(subscription -> log.info(String.format("User login %s", username)));
  }

  @Override
  public Mono<KrakenToken> clientLogin(final SecurityClientCredentialsProperties client) {
    return retry(webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path(this.getOpenIdTokenUrl()).build())
        .body(BodyInserters.fromFormData("client_id", client.getId())
            .with("client_secret", client.getSecret())
            .with("grant_type", "client_credentials"))
        .retrieve()
        .bodyToMono(KrakenToken.class), log)
        .doOnSubscribe(subscription -> log.info(String.format("Client login %s", client.getId())));
  }

  @Override
  public Mono<KrakenToken> exchangeToken(final SecurityClientCredentialsProperties client,
                                         final String accessToken) {
    return retry(webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path(this.getOpenIdTokenUrl()).build())
        .body(BodyInserters.fromFormData("client_id", client.getId())
            .with("client_secret", client.getSecret())
            .with("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange")
            .with("subject_token", accessToken)
            .with("requested_token_type", "urn:ietf:params:oauth:token-type:refresh_token")
            .with("audience", client.getId()))
        .retrieve()
        .bodyToMono(KrakenToken.class), log)
        .doOnSubscribe(subscription -> log.info(String.format("Exchange token %s", accessToken)));
  }

  @Override
  public Mono<KrakenToken> refreshToken(final SecurityClientCredentialsProperties client,
                                        final String refreshToken) {
    return retry(webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path(this.getOpenIdTokenUrl()).build())
        .body(BodyInserters.fromFormData("grant_type", "refresh_token")
            .with("refresh_token", refreshToken)
            .with("client_id", client.getId())
            .with("client_secret", client.getSecret()))
        .retrieve()
        .bodyToMono(KrakenToken.class), log)
        .doOnSubscribe(subscription -> log.info(String.format("Refresh token %s", refreshToken)));
  }

  @Override
  public Mono<KrakenToken> impersonate(final SecurityClientCredentialsProperties client,
                                       final String userId) {
    return retry(webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path(this.getOpenIdTokenUrl()).build())
        .body(BodyInserters.fromFormData("client_id", client.getId())
            .with("client_secret", client.getSecret())
            .with("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange")
            .with("requested_subject", userId))
        .retrieve()
        .bodyToMono(KrakenToken.class), log)
        .doOnSubscribe(subscription -> log.info(String.format("Impersonate %s", userId)));
  }

  private String getOpenIdTokenUrl() {
    return String.format("/realms/%s/protocol/openid-connect/token", this.properties.getRealm());
  }
}
