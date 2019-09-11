package com.kraken.storage.synchronizer;

import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.storage.client.StorageClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class StorageSynchronizer {

  @NonNull StorageClient storageClient;
  @NonNull RuntimeClient runtimeClient;
  @NonNull RuntimeContainerProperties containerProperties;

  @PostConstruct
  public void init() throws Exception {
//    TODO Choper les listes de folder/files a dl ul
//    TODO Appeller le storageClient

    final var setStatusStarting = runtimeClient.setStatus(containerProperties.getContainerId(), ContainerStatus.STARTING);
    final var downloadFiles = Mono.never();
    final var downloadFolders = Mono.never();
    final var setStatusStopping = runtimeClient.setStatus(containerProperties.getContainerId(), ContainerStatus.STOPPING);
    final var waitForStatusStopping = runtimeClient.waitForStatus(containerProperties.getTaskId(), ContainerStatus.STOPPING);
    final var uploadFiles = Mono.never();
    final var setStatusDone = runtimeClient.setStatus(containerProperties.getContainerId(), ContainerStatus.DONE);

    setStatusStarting.then(downloadFiles)
        .then(downloadFolders)
        .then(setStatusStopping)
        .then(waitForStatusStopping)
        .then(uploadFiles)
        .then(setStatusDone)
        .subscribe();
  }

}
