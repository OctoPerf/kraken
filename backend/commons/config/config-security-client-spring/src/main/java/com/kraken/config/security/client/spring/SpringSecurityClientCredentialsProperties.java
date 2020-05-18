package com.kraken.config.security.client.spring;

import com.kraken.config.security.client.api.SecurityClientCredentialsProperties;
import lombok.Builder;
import lombok.Value;
import org.springframework.boot.context.properties.ConstructorBinding;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
final class SpringSecurityClientCredentialsProperties implements SecurityClientCredentialsProperties {

  String id;
  String secret;

  SpringSecurityClientCredentialsProperties(final String id,
                                            final String secret) {
    this.id = requireNonNull(id);
    this.secret = ofNullable(secret).orElse("");
  }
}
