package com.octoperf.kraken.config.security.client.spring;

import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

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

  SpringSecurityClientProperties(@NonNull final String url,
                                 final SpringSecurityClientCredentialsProperties web,
                                 final SpringSecurityClientCredentialsProperties api,
                                 final SpringSecurityClientCredentialsProperties container,
                                 final String realm) {
    this.url = url;
    this.web = ofNullable(web).orElse(new SpringSecurityClientCredentialsProperties("kraken-web", ""));
    this.api = ofNullable(api).orElse(new SpringSecurityClientCredentialsProperties("kraken-api", ""));
    this.container = ofNullable(container).orElse(new SpringSecurityClientCredentialsProperties("kraken-container", ""));
    this.realm = realm;
  }
}
