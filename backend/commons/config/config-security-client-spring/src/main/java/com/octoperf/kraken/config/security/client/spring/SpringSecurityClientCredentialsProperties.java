package com.octoperf.kraken.config.security.client.spring;

import com.octoperf.kraken.config.security.client.api.SecurityClientCredentialsProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConstructorBinding;

import static java.util.Optional.ofNullable;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
final class SpringSecurityClientCredentialsProperties implements SecurityClientCredentialsProperties {

  String id;
  String secret;

  SpringSecurityClientCredentialsProperties(@NonNull final String id,
                                            final String secret) {
    this.id = id;
    this.secret = ofNullable(secret).orElse("");
  }
}
