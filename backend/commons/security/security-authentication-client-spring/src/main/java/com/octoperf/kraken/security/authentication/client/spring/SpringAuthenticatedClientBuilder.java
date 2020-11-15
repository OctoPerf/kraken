package com.octoperf.kraken.security.authentication.client.spring;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.security.authentication.api.UserProviderFactory;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClient;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuilder;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerType;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.SESSION;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@AllArgsConstructor
public abstract class SpringAuthenticatedClientBuilder<T extends AuthenticatedClient> implements AuthenticatedClientBuilder<T> {

  @NonNull List<UserProviderFactory> userProviderFactories;

  protected Mono<Owner> getOwner(final AuthenticatedClientBuildOrder order) {
    switch (order.getMode()) {
      case SESSION:
        return userProviderFactories.stream().filter(userProviderFactory -> userProviderFactory.getMode().equals(SESSION))
            .findFirst().orElseThrow()
            .create("") // User Id is retrieved from connected user
            .getOwner(order.getApplicationId(), order.getProjectId());
      case IMPERSONATE:
        return Mono.just(Owner.builder()
            .userId(order.getUserId())
            .projectId(order.getProjectId())
            .applicationId(order.getApplicationId())
            .roles(ImmutableList.of(KrakenRole.USER))
            .type(OwnerType.USER)
            .build());
      case SERVICE_ACCOUNT:
        return Mono.just(Owner.builder()
            .userId(order.getUserId())
            .projectId(order.getProjectId())
            .applicationId(order.getApplicationId())
            .roles(ImmutableList.of(KrakenRole.ADMIN, KrakenRole.API))
            .type(OwnerType.USER)
            .build());
      case CONTAINER:
        return Mono.error(new IllegalArgumentException("The CONTAINER mode is not supported"));
      default:
        return Mono.just(Owner.PUBLIC);
    }
  }
}
