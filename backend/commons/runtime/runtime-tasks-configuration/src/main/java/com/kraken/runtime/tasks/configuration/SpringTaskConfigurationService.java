package com.kraken.runtime.tasks.configuration;

import com.kraken.runtime.entity.task.TaskType;
import com.kraken.config.runtime.server.api.RuntimeServerProperties;
import com.kraken.runtime.tasks.configuration.entity.TaskConfiguration;
import com.kraken.runtime.tasks.configuration.entity.TasksConfiguration;
import com.kraken.storage.client.StorageClient;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;


@Slf4j
@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class SpringTaskConfigurationService implements TaskConfigurationService {
  @NonNull RuntimeServerProperties server;
  @NonNull StorageClient storageClient;

  public Mono<TaskConfiguration> getConfiguration(final TaskType taskType) {
    return storageClient
      .getYamlContent(server.getConfigPath(), TasksConfiguration.class)
      .map(config -> config.getTasks()
        .stream()
        .filter(task -> Objects.equals(task.getType(), taskType))
        .findFirst())
      .filter(Optional::isPresent)
      .map(Optional::get);
  }

}
