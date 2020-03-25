package com.kraken.storage.configuration;

// https://blog.ineat-group.com/2019/01/securiser-vos-apis-spring-webflux-avec-keycloak-et-oauth2/

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;

@EnableWebFluxSecurity
public class SecurityConfiguration {
  @Bean
  SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http, final Converter<Jwt, Mono<AbstractAuthenticationToken>> converter) {
    http
        .authorizeExchange()
        .pathMatchers("/static/**")
        .permitAll()
        .anyExchange().authenticated()
        .and()
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(converter);
    return http.build();
  }

  @Bean
  Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
    GrantedAuthoritiesExtractor extractor = new GrantedAuthoritiesExtractor();
    return new ReactiveJwtAuthenticationConverterAdapter(extractor);
  }

  static class GrantedAuthoritiesExtractor extends JwtAuthenticationConverter {
    @Override
    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
      System.out.println(jwt.getClaims());
      System.out.println(jwt.getHeaders());
//      {sub=db5d1288-9447-4742-b53e-63fcd0564e01, resource_access={"account":{"roles":["manage-account","manage-account-links","view-profile"]}}, email_verified=true, iss=http://localhost:9080/auth/realms/kraken, groups=[], typ=Bearer, preferred_username=kraken-admin, aud=[account], acr=1, realm_access={"roles":["offline_access","uma_authorization","ADMIN"]}, azp=kraken-web, scope=email profile, exp=2020-03-25T16:58:05Z, session_state=1368e338-30f3-43ad-91d0-d2aff9581885, iat=2020-03-25T16:53:05Z, jti=97999b83-551e-4ae4-85f1-4cf20d6f6a77}
//        {kid=Yo_KOb3yFgpis3NmOQv8Q47fIBYmnJldKeDMKCYAA8c, typ=JWT, alg=RS256}

//      {sub=2e44ffae-111c-4f59-ae2b-65000de6f7b7, resource_access={"account":{"roles":["manage-account","manage-account-links","view-profile"]}}, email_verified=true, iss=http://localhost:9080/auth/realms/kraken, groups=[], typ=Bearer, preferred_username=kraken-user, aud=[account], acr=1, realm_access={"roles":["offline_access","uma_authorization","USER"]}, azp=kraken-web, scope=email profile, exp=2020-03-25T16:59:48Z, session_state=d7f2cf49-48bf-4637-8882-c24fe7a59bf8, iat=2020-03-25T16:54:48Z, jti=06f6b8b1-b56f-4712-9295-b66f8fe60252}
//        {kid=Yo_KOb3yFgpis3NmOQv8Q47fIBYmnJldKeDMKCYAA8c, typ=JWT, alg=RS256}

//      {sub=2e44ffae-111c-4f59-ae2b-65000de6f7b7, resource_access={"account":{"roles":["manage-account","manage-account-links","view-profile"]}}, email_verified=true, iss=http://localhost:9080/auth/realms/kraken, groups=[], typ=Bearer, preferred_username=kraken-user, aud=[account], acr=1, realm_access={"roles":["offline_access","uma_authorization","USER"]}, azp=kraken-web, scope=email profile, exp=2020-03-25T16:59:48Z, session_state=d7f2cf49-48bf-4637-8882-c24fe7a59bf8, iat=2020-03-25T16:54:48Z, jti=06f6b8b1-b56f-4712-9295-b66f8fe60252}
//        {kid=Yo_KOb3yFgpis3NmOQv8Q47fIBYmnJldKeDMKCYAA8c, typ=JWT, alg=RS256}

//      {sub=2e44ffae-111c-4f59-ae2b-65000de6f7b7, resource_access={"account":{"roles":["manage-account","manage-account-links","view-profile"]}}, email_verified=true, iss=http://localhost:9080/auth/realms/kraken, groups=[], typ=Bearer, preferred_username=kraken-user, aud=[account], acr=1, realm_access={"roles":["offline_access","uma_authorization","USER"]}, azp=kraken-web, scope=email profile, exp=2020-03-25T16:59:48Z, session_state=d7f2cf49-48bf-4637-8882-c24fe7a59bf8, iat=2020-03-25T16:54:48Z, jti=06f6b8b1-b56f-4712-9295-b66f8fe60252}
//        {kid=Yo_KOb3yFgpis3NmOQv8Q47fIBYmnJldKeDMKCYAA8c, typ=JWT, alg=RS256}

        return super.extractAuthorities(jwt);
    }
  }
}
