package com.kraken.security.entity.functions.spring;

import com.google.common.collect.ImmutableMap;
import com.kraken.security.entity.functions.api.OwnerToUserId;
import com.kraken.security.entity.owner.Owner;
import com.kraken.security.entity.owner.OwnerType;
import com.kraken.security.entity.owner.UserOwner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
final class SpringOwnerToUserId implements OwnerToUserId {

  private static final Map<OwnerType, OwnerToUserId> MAPPERS = ImmutableMap.of(
      OwnerType.USER, (final Owner owner) -> Optional.of(((UserOwner) owner).getUserId())
  );

  private static final OwnerToUserId EMPTY = (final Owner owner) -> Optional.empty();

  @Override
  public Optional<String> apply(final Owner owner) {
    return MAPPERS.getOrDefault(owner.getType(), EMPTY).apply(owner);
  }
}
