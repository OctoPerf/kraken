package com.octoperf.kraken.security.authentication.client.spring;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.api.UserProviderFactory;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClient;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuilder;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.PublicOwner;
import com.octoperf.kraken.security.entity.owner.UserOwner;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.*;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class SpringAuthenticatedClientBuilder<T extends AuthenticatedClient> implements AuthenticatedClientBuilder<T> {

  final List<UserProviderFactory> userProviderFactories;
  Optional<AuthenticationMode> mode;
  Optional<String> userId;
  Optional<String> applicationId;

  protected SpringAuthenticatedClientBuilder(@NonNull final List<UserProviderFactory> userProviderFactories) {
    this.userProviderFactories = userProviderFactories;
    this.mode = Optional.of(NOOP);
    this.userId = Optional.empty();
    this.applicationId = Optional.empty();
  }

  @Override
  public AuthenticatedClientBuilder<T> mode(AuthenticationMode mode) {
    checkArgument(!mode.equals(CONTAINER), "The CONTAINER mode is not supported");
    this.mode = Optional.of(mode);
    return this;
  }

  @Override
  public AuthenticatedClientBuilder<T> mode(AuthenticationMode mode, String userId) {
    this.mode(mode);
    this.userId = Optional.of(userId);
    return this;
  }

  @Override
  public AuthenticatedClientBuilder<T> applicationId(String applicationId) {
    this.applicationId = Optional.of(applicationId);
    return this;
  }

  protected Mono<Owner> getOwner() {
    switch (mode.orElseThrow()) {
      case SESSION:
        return userProviderFactories.stream().filter(userProviderFactory -> userProviderFactory.getMode().equals(SESSION))
            .findFirst().orElseThrow()
            .create("") // User Id is retrieved from connected user
            .getOwner(this.applicationId.orElseThrow());
      case IMPERSONATE:
        return Mono.just(UserOwner.builder()
            .userId(userId.orElseThrow())
            .applicationId(applicationId.orElseThrow())
            .roles(ImmutableList.of(KrakenRole.USER)).build());
      default:
        return Mono.just(PublicOwner.INSTANCE);
    }
  }
}
