package com.kraken.storage.file;

import com.kraken.security.entity.owner.ApplicationOwner;
import com.kraken.security.entity.owner.Owner;
import com.kraken.security.entity.owner.UserOwner;
import com.kraken.security.entity.token.KrakenRole;
import io.methvin.watcher.DirectoryChangeEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static com.kraken.security.entity.owner.OwnerType.USER;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class FileSystemStorageServiceBuilder implements StorageServiceBuilder {

  @NonNull Flux<DirectoryChangeEvent> watcherEventFlux;
  @NonNull OwnerToPath ownerToPath;

  @Override
  public StorageService build(final Owner owner) {
    final var root = ownerToPath.apply(owner);
    final var service = new FileSystemStorageService(root, new FileSystemPathToStorageNode(root), watcherEventFlux);
    if (owner.getType().equals(USER)) {
      final UserOwner userOwner = (UserOwner) owner;
      if (!userOwner.getRoles().contains(KrakenRole.ADMIN)) {
        final var applicationPath = ownerToPath.apply(ApplicationOwner.builder().applicationId(userOwner.getApplicationId()).build());
        service.init(applicationPath);
      }
    }
    return service;
  }

}
