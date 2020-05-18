package com.kraken.config.security.client.spring;

import com.kraken.config.security.client.api.SecurityClientProperties;
import lombok.Builder;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
@ConfigurationProperties("kraken.security")
final class SpringSecurityClientProperties implements SecurityClientProperties {

  String url;
  SpringSecurityClientCredentialsProperties web;
  SpringSecurityClientCredentialsProperties api;
  SpringSecurityClientCredentialsProperties container;
  String realm;

  SpringSecurityClientProperties(final String url,
                                 final SpringSecurityClientCredentialsProperties web,
                                 final SpringSecurityClientCredentialsProperties api,
                                 final SpringSecurityClientCredentialsProperties container,
                                 final String realm) {
    this.url = requireNonNull(url);
    this.web = ofNullable(web).orElse(new SpringSecurityClientCredentialsProperties("kraken-web", ""));
    this.api = ofNullable(api).orElse(new SpringSecurityClientCredentialsProperties("kraken-api", ""));
    this.container = ofNullable(container).orElse(new SpringSecurityClientCredentialsProperties("kraken-container", ""));
    this.realm = realm;
  }
}
