package com.octoperf.kraken.security.authentication.api;

import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerType;
import com.octoperf.kraken.security.entity.token.KrakenTokenUser;
import reactor.core.publisher.Mono;

public interface UserProvider {

  default Mono<Owner> getOwner(final String applicationId, final String projectId) {
    return this.getAuthenticatedUser().map(user -> Owner.builder()
        .applicationId(applicationId)
        .projectId(projectId)
        .userId(user.getUserId())
        .type(OwnerType.USER)
        .roles(user.getRoles()).build());
  }

  Mono<KrakenTokenUser> getAuthenticatedUser();

  Mono<String> getTokenValue();

}
