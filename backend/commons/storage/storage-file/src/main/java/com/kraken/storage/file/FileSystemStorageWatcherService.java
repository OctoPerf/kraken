package com.kraken.storage.file;

import com.kraken.storage.entity.StorageWatcherEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class FileSystemStorageWatcherService implements StorageWatcherService {

  @NonNull Flux<StorageWatcherEvent> watcherEventFlux;

  @Override
  public Flux<StorageWatcherEvent> watch() {
    return Flux.from(watcherEventFlux);
  }

  @Override
  public Flux<StorageWatcherEvent> watch(final String root) {
    return Flux.from(watcherEventFlux)
        .filter(storageWatcherEvent -> storageWatcherEvent.getNode().getPath().startsWith(root));
  }

}

