package com.octoperf.kraken.security.admin.client.keycloak;

import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
import com.octoperf.kraken.security.admin.client.api.SecurityAdminClient;
import com.octoperf.kraken.security.admin.client.api.SecurityAdminClientBuilder;
import com.octoperf.kraken.security.authentication.api.ExchangeFilterFactory;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.authentication.client.spring.WebAuthenticatedClientBuilder;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class KeycloakSecurityAdminClientBuilder extends WebAuthenticatedClientBuilder<SecurityAdminClient, SecurityClientProperties> implements SecurityAdminClientBuilder {

  public KeycloakSecurityAdminClientBuilder(final List<ExchangeFilterFactory> exchangeFilterFactories,
                                            final SecurityClientProperties properties) {
    super(exchangeFilterFactories, properties);
  }

  @Override
  public Mono<SecurityAdminClient> build(final AuthenticatedClientBuildOrder order) {
    return Mono.just(new KeycloakSecurityAdminClient(getWebClientBuilder(order).build(), properties));
  }

}
