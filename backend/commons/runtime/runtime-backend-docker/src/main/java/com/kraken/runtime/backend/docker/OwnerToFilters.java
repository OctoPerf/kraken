package com.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.kraken.security.entity.functions.api.OwnerToApplicationId;
import com.kraken.security.entity.functions.api.OwnerToUserId;
import com.kraken.security.entity.owner.Owner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static com.kraken.runtime.backend.api.EnvironmentLabels.*;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class OwnerToFilters implements Function<Owner, List<String>> {

  @NonNull OwnerToApplicationId toApplicationId;
  @NonNull OwnerToUserId toUserId;

  @Override
  public List<String> apply(final Owner owner) {
    final var listBuilder = ImmutableList.<String>builder();
    final var applicationId = toApplicationId.apply(owner);
    final var userId = toUserId.apply(owner);
    applicationId.ifPresent(appId -> listBuilder.add("--filter", String.format("label=%s=%s", COM_KRAKEN_APPLICATION_ID, appId)));
    userId.ifPresent(uId -> listBuilder.add("--filter", String.format("label=%s=%s", COM_KRAKEN_USER_ID, uId)));
    return listBuilder.build();
  }

}
