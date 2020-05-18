package com.kraken.security.user.events.listener;

import reactor.core.publisher.Mono;

public abstract class UserEventsServiceAdapter implements UserEventsService {
  @Override
  public Mono<String> onRegisterUser(final String userId, final String email, final String username) {
    return Mono.just(userId);
  }

  @Override
  public Mono<String> onUpdateEmail(final String userId, final String updatedEmail, final String previousEmail) {
    return Mono.just(userId);
  }

  @Override
  public Mono<String> onDeleteUser(final String userId) {
    return Mono.just(userId);
  }

  @Override
  public Mono<String> onCreateRole(final String userId, final String role) {
    return Mono.just(userId);
  }

  @Override
  public Mono<String> onDeleteRole(final String userId, final String role) {
    return Mono.just(userId);
  }
}
