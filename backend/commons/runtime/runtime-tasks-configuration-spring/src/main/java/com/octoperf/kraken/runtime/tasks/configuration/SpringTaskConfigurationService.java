package com.octoperf.kraken.runtime.tasks.configuration;

import com.octoperf.kraken.config.runtime.server.api.RuntimeServerProperties;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.runtime.tasks.configuration.entity.TaskConfiguration;
import com.octoperf.kraken.runtime.tasks.configuration.entity.TasksConfiguration;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.client.api.StorageClientBuilder;
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

  public Mono<TaskConfiguration> getConfiguration(final Owner owner, final TaskType taskType) {
    return this.ownerToStorageClient(owner)
        .flatMap(storageClient -> storageClient.getYamlContent(server.getConfigPath(), TasksConfiguration.class))
        .map(config -> config.getTasks()
            .stream()
            .filter(task -> Objects.equals(task.getType(), taskType))
            .findFirst())
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  @Override
  public Mono<String> getTemplate(final Owner owner, final String file) {
    return this.ownerToStorageClient(owner).flatMap(storageClient -> storageClient.getContent(file));
  }

  private Mono<StorageClient> ownerToStorageClient(final Owner owner) {
    return storageClientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .serviceAccount(owner)
        .build());
  }
}
