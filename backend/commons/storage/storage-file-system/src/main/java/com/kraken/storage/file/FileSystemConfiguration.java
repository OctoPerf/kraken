package com.kraken.storage.file;

import com.kraken.config.api.ApplicationProperties;
import com.kraken.storage.entity.StorageWatcherEvent;
import io.methvin.watcher.DirectoryChangeEvent;
import io.methvin.watcher.DirectoryChangeListener;
import io.methvin.watcher.DirectoryWatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;

@Slf4j
@Configuration
class FileSystemConfiguration {

  @Bean
  Flux<DirectoryChangeEvent> watcherEventFlux(
      final ApplicationProperties applicationProperties
  ) {
    return Flux.<DirectoryChangeEvent>create(emitter -> {
      try {
        final var watcher = DirectoryWatcher.builder()
            .path(Paths.get(applicationProperties.getData()))
            .listener(new DirectoryChangeListener() {
              @Override
              public void onEvent(DirectoryChangeEvent event) {
                emitter.next(event);
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