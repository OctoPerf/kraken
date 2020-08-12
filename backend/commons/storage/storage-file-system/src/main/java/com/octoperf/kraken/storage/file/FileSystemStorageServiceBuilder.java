package com.octoperf.kraken.storage.file;

import com.octoperf.kraken.security.entity.owner.ApplicationOwner;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.UserOwner;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import com.octoperf.kraken.tools.event.bus.EventBus;
import io.methvin.watcher.DirectoryChangeEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static com.octoperf.kraken.security.entity.owner.OwnerType.USER;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class FileSystemStorageServiceBuilder implements StorageServiceBuilder {

  @NonNull OwnerToPath ownerToPath;
  @NonNull EventBus eventBus;

  @Override
  public StorageService build(final Owner owner) {
    final var root = ownerToPath.apply(owner);
    log.debug(String.format("Init storage service for owner %s - path: %s", owner.toString(), root.toString()));
    final var service = new FileSystemStorageService(owner, root, new FileSystemPathToStorageNode(root), eventBus);
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
