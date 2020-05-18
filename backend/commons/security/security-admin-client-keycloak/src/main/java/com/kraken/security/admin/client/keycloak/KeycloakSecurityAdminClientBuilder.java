package com.kraken.security.admin.client.keycloak;

import com.kraken.config.security.client.api.SecurityClientProperties;
import com.kraken.security.admin.client.api.SecurityAdminClient;
import com.kraken.security.admin.client.api.SecurityAdminClientBuilder;
import com.kraken.security.authentication.api.ExchangeFilterFactory;
import com.kraken.security.authentication.client.spring.AbstractAuthenticatedClientBuilder;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class KeycloakSecurityAdminClientBuilder extends AbstractAuthenticatedClientBuilder<SecurityAdminClient, SecurityClientProperties> implements SecurityAdminClientBuilder {

  public KeycloakSecurityAdminClientBuilder(final List<ExchangeFilterFactory> exchangeFilterFactories,
                                            final SecurityClientProperties properties) {
    super(exchangeFilterFactories, properties);
  }

  @Override
  public SecurityAdminClient build() {
    return new KeycloakSecurityAdminClient(webClientBuilder.build(), properties);
  }

}
