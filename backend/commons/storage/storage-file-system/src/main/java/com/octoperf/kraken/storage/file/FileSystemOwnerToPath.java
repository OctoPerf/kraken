package com.octoperf.kraken.storage.file;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.config.api.ApplicationProperties;
import com.octoperf.kraken.security.entity.owner.ApplicationOwner;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerType;
import com.octoperf.kraken.security.entity.owner.UserOwner;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class FileSystemOwnerToPath implements OwnerToPath {

  private static final Map<OwnerType, Function<Owner, Path>> MAPPERS = ImmutableMap.of(
      OwnerType.PUBLIC, (final Owner owner) -> Path.of("public"),
      OwnerType.APPLICATION, (@NonNull final Owner owner) -> Path.of("applications", ((ApplicationOwner) owner).getApplicationId()),
      OwnerType.USER, (@NonNull final Owner owner) -> {
        final UserOwner userOwner = ((UserOwner) owner);
        if (userOwner.getRoles().contains(KrakenRole.ADMIN)) {
          // Admin users can edit default files for every application
          return Path.of("applications");
        }
        return Path.of("users", userOwner.getUserId(), userOwner.getApplicationId());
      }
  );

  @NonNull
  ApplicationProperties properties;

  @Override
  public Path apply(@NonNull final Owner owner) {
    final var ownerPath = MAPPERS.get(owner.getType()).apply(owner);
    return Paths.get(properties.getData()).resolve(ownerPath);
  }
}
