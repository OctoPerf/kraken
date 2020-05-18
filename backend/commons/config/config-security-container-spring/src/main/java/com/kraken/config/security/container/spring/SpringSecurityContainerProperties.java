package com.kraken.config.security.container.spring;

import com.kraken.config.security.container.api.SecurityContainerProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@Value
@ConstructorBinding
@ConfigurationProperties("kraken.security")
final class SpringSecurityContainerProperties implements SecurityContainerProperties {

  String accessToken;
  String refreshToken;
  Long minValidity; // seconds
  Long refreshMinValidity; // seconds
  Long expiresIn; // seconds
  Long refreshExpiresIn; // seconds

  @Builder(toBuilder = true)
  SpringSecurityContainerProperties(@NonNull final String accessToken,
                                    @NonNull final String refreshToken,
                                    final Long minValidity,
                                    final Long refreshMinValidity,
                                    @NonNull final Long expiresIn,
                                    @NonNull final Long refreshExpiresIn) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.minValidity = ofNullable(minValidity).orElse(60L);
    this.refreshMinValidity = ofNullable(refreshMinValidity).orElse(300L);
    this.expiresIn = expiresIn;
    this.refreshExpiresIn = refreshExpiresIn;
  }

}
