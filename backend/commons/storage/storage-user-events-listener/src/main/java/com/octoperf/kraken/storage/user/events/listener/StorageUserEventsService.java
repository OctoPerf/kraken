package com.octoperf.kraken.storage.user.events.listener;

import com.octoperf.kraken.config.api.ApplicationProperties;
import com.octoperf.kraken.security.user.events.listener.UserEventsServiceAdapter;
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
  public Mono<String> onRegisterUser(String userId, String email, String username) {
    return Mono.fromCallable(() -> {
      log.info(String.format("Creating user folder for id %s", userId));
      if (!Paths.get(properties.getData(), "users", userId).toFile().mkdirs()) {
        throw new RuntimeException("Failed to create user directory");
      }
      return userId;
    });
  }

  @Override
  public Mono<String> onDeleteUser(final String userId) {
    return Mono.fromCallable(() -> {
      log.info(String.format("Deleting user folder for id %s", userId));
      deleteRecursively(Paths.get(properties.getData(), "users", userId));
      return userId;
    });
  }
}
