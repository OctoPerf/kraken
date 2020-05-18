package com.kraken.storage.user.events.listener;

import com.kraken.config.api.ApplicationProperties;
import com.kraken.security.user.events.listener.UserEventsServiceAdapter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.file.Paths;

import static org.springframework.util.FileSystemUtils.deleteRecursively;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
final class StorageUserEventsService extends UserEventsServiceAdapter {

  @NonNull
  ApplicationProperties properties;

  @Override
  public Mono<String> onDeleteUser(final String userId) {
    return Mono.fromCallable(() -> {
      log.info(String.format("Deleting user folder for id %s", userId));
      deleteRecursively(Paths.get(properties.getData(), "users", userId));
      return userId;
    });
  }
}
