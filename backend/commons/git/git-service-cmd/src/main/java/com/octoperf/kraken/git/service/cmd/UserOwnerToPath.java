package com.octoperf.kraken.git.service.cmd;

import com.octoperf.kraken.config.api.ApplicationProperties;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.OwnerType;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkArgument;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class UserOwnerToPath implements OwnerToPath {

  @NonNull
  ApplicationProperties properties;

  @Override
  public Path apply(@NonNull final Owner owner) {
    checkArgument(OwnerType.USER.equals(owner.getType()));
    checkArgument(!owner.getUserId().isEmpty());
    checkArgument(!owner.getRoles().contains(KrakenRole.ADMIN));
    final var ownerPath = Path.of("users", owner.getUserId(), owner.getProjectId(), owner.getApplicationId());
    return Paths.get(properties.getData()).resolve(ownerPath);
  }
}
