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
@SuppressWarnings("squid:S2068")
final class KeycloakSecurityClient implements SecurityClient {

  private static final String CLIENT_ID = "client_id";
  private static final String CLIENT_SECRET = "client_secret";
  private static final String REFRESH_TOKEN = "refresh_token";
  private static final String GRANT_TYPE = "grant_type";
  private static final String PASSWORD = "password";
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
            .with(PASSWORD, password)
            .with(GRANT_TYPE, PASSWORD)
            .with(CLIENT_ID, client.getId()))
        .retrieve()
        .bodyToMono(KrakenToken.class), log)
        .doOnSubscribe(subscription -> log.info(String.format("User login %s", username)));
  }

  @Override
  public Mono<KrakenToken> clientLogin(final SecurityClientCredentialsProperties client) {
    return retry(webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path(this.getOpenIdTokenUrl()).build())
        .body(BodyInserters.fromFormData(CLIENT_ID, client.getId())
            .with(CLIENT_SECRET, client.getSecret())
            .with(GRANT_TYPE, "client_credentials"))
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
        .body(BodyInserters.fromFormData(CLIENT_ID, client.getId())
            .with(CLIENT_SECRET, client.getSecret())
            .with(GRANT_TYPE, "urn:ietf:params:oauth:grant-type:token-exchange")
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
        .body(BodyInserters.fromFormData(GRANT_TYPE, REFRESH_TOKEN)
            .with(REFRESH_TOKEN, refreshToken)
            .with(CLIENT_ID, client.getId())
            .with(CLIENT_SECRET, client.getSecret()))
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
        .body(BodyInserters.fromFormData(CLIENT_ID, client.getId())
            .with(CLIENT_SECRET, client.getSecret())
            .with(GRANT_TYPE, "urn:ietf:params:oauth:grant-type:token-exchange")
            .with("requested_subject", userId))
        .retrieve()
        .bodyToMono(KrakenToken.class), log)
        .doOnSubscribe(subscription -> log.info(String.format("Impersonate %s", userId)));
  }

  private String getOpenIdTokenUrl() {
    return String.format("/realms/%s/protocol/openid-connect/token", this.properties.getRealm());
  }
}
