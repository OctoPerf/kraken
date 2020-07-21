package com.octoperf.kraken.security.configuration;

import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class KrakenServerAuthenticationConverter implements ServerAuthenticationConverter {

  public static final String JWT_COOKIE_NAME = "JWT";

  ServerAuthenticationConverter defaultConverter = new ServerBearerTokenAuthenticationConverter();

  @Override
  public Mono<Authentication> convert(final ServerWebExchange serverWebExchange) {
    return defaultConverter.convert(serverWebExchange).switchIfEmpty(convertCookie(serverWebExchange));
  }

  private Mono<Authentication> convertCookie(final ServerWebExchange exchange) {
    return Mono.justOrEmpty(exchange.getRequest().getCookies().getFirst(JWT_COOKIE_NAME))
        .map(HttpCookie::getValue)
        .map(BearerTokenAuthenticationToken::new);
  }
}
