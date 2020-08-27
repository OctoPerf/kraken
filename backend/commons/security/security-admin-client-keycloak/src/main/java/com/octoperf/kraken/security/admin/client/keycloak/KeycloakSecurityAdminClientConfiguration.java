package com.octoperf.kraken.security.admin.client.keycloak;

import com.octoperf.kraken.security.admin.client.api.SecurityAdminClient;
import com.octoperf.kraken.security.admin.client.api.SecurityAdminClientBuilder;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
class KeycloakSecurityAdminClientConfiguration {

  @Bean
  Mono<SecurityAdminClient> securityAdminClient(final SecurityAdminClientBuilder builder) {
    return builder.build(AuthenticatedClientBuildOrder.builder().mode(AuthenticationMode.SERVICE_ACCOUNT).build());
  }
}
