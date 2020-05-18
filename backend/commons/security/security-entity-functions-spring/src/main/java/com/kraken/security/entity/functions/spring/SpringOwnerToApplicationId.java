package com.kraken.security.entity.functions.spring;

import com.google.common.collect.ImmutableMap;
import com.kraken.security.entity.functions.api.OwnerToApplicationId;
import com.kraken.security.entity.owner.ApplicationOwner;
import com.kraken.security.entity.owner.Owner;
import com.kraken.security.entity.owner.OwnerType;
import com.kraken.security.entity.owner.UserOwner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
final class SpringOwnerToApplicationId implements OwnerToApplicationId {

  private static final Map<OwnerType, OwnerToApplicationId> MAPPERS = ImmutableMap.of(
      OwnerType.APPLICATION, (final Owner owner) -> Optional.of(((ApplicationOwner) owner).getApplicationId()),
      OwnerType.USER, (final Owner owner) -> Optional.of(((UserOwner) owner).getApplicationId())
  );

  private static final OwnerToApplicationId EMPTY = (final Owner owner) -> Optional.empty();

  @Override
  public Optional<String> apply(final Owner owner) {
    return MAPPERS.getOrDefault(owner.getType(), EMPTY).apply(owner);
  }
}
