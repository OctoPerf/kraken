package com.kraken.security.admin.client.keycloak;

import com.kraken.security.admin.client.api.SecurityAdminClient;
import com.kraken.security.admin.client.api.SecurityAdminClientBuilder;
import com.kraken.security.authentication.api.AuthenticationMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakSecurityAdminClientConfiguration {

  @Bean
  SecurityAdminClient securityAdminClient(final SecurityAdminClientBuilder builder){
    return builder.mode(AuthenticationMode.SERVICE_ACCOUNT).build();
  }
}
