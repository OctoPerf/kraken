package com.kraken.storage.file;

import com.kraken.tools.configuration.properties.ApplicationProperties;
import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageWatcherEvent;
import io.methvin.watcher.DirectoryChangeEvent;
import io.methvin.watcher.DirectoryChangeListener;
import io.methvin.watcher.DirectoryWatcher;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class FileSystemStorageWatcherService implements StorageWatcherService {

  @NonNull
  ApplicationProperties applicationProperties;

  @NonNull
  Function<Path, StorageNode> toStorageNode;

  @Override
  public Flux<StorageWatcherEvent> watch() {
    return Flux.create(emitter -> {
      try {
        final var watcher = DirectoryWatcher.builder()
            .path(applicationProperties.getData())
            .listener(new DirectoryChangeListener() {
              @Override
              public void onEvent(DirectoryChangeEvent event) {
                final var fileEvent = StorageWatcherEvent.builder()
                    .node(toStorageNode.apply(event.path()))
                    .event(event.eventType().toString())
                    .build();
                emitter.next(fileEvent);
              }

              @Override
              public void onException(Exception e) {
                emitter.error(e);
              }
            })
            .build();
        emitter.onDispose(() -> {
          try {
            watcher.close();
          } catch (IOException e) {
            log.error("Failed to close watcher", e);
          }
        });
        watcher.watchAsync();
      } catch (IOException e) {
        emitter.error(e);
        emitter.complete();
      }
    });
  }

}

