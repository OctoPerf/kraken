package com.octoperf.kraken.security.client.keycloak;

import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
import com.octoperf.kraken.security.client.api.SecurityClient;
import com.octoperf.kraken.security.client.api.SecurityClientBuilder;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
class KeycloakSecurityClientBuilder implements SecurityClientBuilder {

  @NonNull
  SecurityClientProperties properties;

  @Override
  public Mono<SecurityClient> build() {
    return Mono.just(new KeycloakSecurityClient(properties, WebClient
        .builder()
        .baseUrl(properties.getUrl())
        .build()));
  }
}
