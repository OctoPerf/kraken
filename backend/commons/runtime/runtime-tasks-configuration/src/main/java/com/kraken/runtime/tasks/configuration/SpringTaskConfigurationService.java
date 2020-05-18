package com.kraken.runtime.tasks.configuration;

import com.kraken.config.runtime.server.api.RuntimeServerProperties;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.runtime.tasks.configuration.entity.TaskConfiguration;
import com.kraken.runtime.tasks.configuration.entity.TasksConfiguration;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.security.entity.functions.api.OwnerToApplicationId;
import com.kraken.security.entity.owner.Owner;
import com.kraken.storage.client.api.StorageClient;
import com.kraken.storage.client.api.StorageClientBuilder;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;


@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class SpringTaskConfigurationService implements TaskConfigurationService {
  @NonNull RuntimeServerProperties server;
  @NonNull StorageClientBuilder storageClientBuilder;
  @NonNull OwnerToApplicationId toApplicationId;

  public Mono<TaskConfiguration> getConfiguration(final Owner owner, final TaskType taskType) {
    return storageClientBuilder.applicationId(toApplicationId.apply(owner).orElseThrow()).mode(AuthenticationMode.SESSION).build()
        .getYamlContent(server.getConfigPath(), TasksConfiguration.class)
        .map(config -> config.getTasks()
            .stream()
            .filter(task -> Objects.equals(task.getType(), taskType))
            .findFirst())
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  @Override
  public Mono<String> getTemplate(final Owner owner, final String file) {
    return storageClientBuilder.applicationId(toApplicationId.apply(owner).orElseThrow()).mode(AuthenticationMode.SESSION).build().getContent(file);
  }
}
