package com.kraken.runtime.tasks.configuration;

import com.kraken.runtime.entity.task.TaskType;
import com.kraken.runtime.server.properties.RuntimeServerProperties;
import com.kraken.runtime.tasks.configuration.entity.TaskConfiguration;
import com.kraken.runtime.tasks.configuration.entity.TasksConfiguration;
import com.kraken.storage.client.StorageClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Component
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringTaskConfigurationService implements TaskConfigurationService {

  @NonNull RuntimeServerProperties runtimeServerProperties;
  @NonNull StorageClient storageClient;

  public Mono<TaskConfiguration> getConfiguration(final TaskType taskType) {
    return storageClient.getYamlContent(runtimeServerProperties.getConfigurationPath(), TasksConfiguration.class)
        .map(tasksConfiguration -> tasksConfiguration.getTasks()
            .stream()
            .filter(taskConfiguration -> taskConfiguration.getType().equals(taskType))
            .findFirst())
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

}
