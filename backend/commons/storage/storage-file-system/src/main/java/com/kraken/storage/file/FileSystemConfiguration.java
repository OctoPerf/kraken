package com.kraken.storage.file;

import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageWatcherEvent;
import com.kraken.config.api.ApplicationProperties;
import io.methvin.watcher.DirectoryChangeEvent;
import io.methvin.watcher.DirectoryChangeListener;
import io.methvin.watcher.DirectoryWatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.function.Function;

@Slf4j
@Configuration
public class FileSystemConfiguration {

  @Bean
  Flux<StorageWatcherEvent> watcherEventFlux(
      final ApplicationProperties kraken,
      final Function<Path, StorageNode> toStorageNode
  ) {
    return Flux.<StorageWatcherEvent>create(emitter -> {
      try {
        final var watcher = DirectoryWatcher.builder()
            .path(Paths.get(kraken.getData()))
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
    }).share() // Avoids creating a watcher for every subscriber
      .retryBackoff(Integer.MAX_VALUE, Duration.ofSeconds(5))
      .onErrorContinue((throwable, o) -> log.error("Failed to watch file " + o, throwable));
  }

}