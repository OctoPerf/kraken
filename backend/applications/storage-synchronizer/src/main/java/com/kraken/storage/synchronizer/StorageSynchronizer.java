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
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class StorageSynchronizer {

  @NonNull StorageClient storageClient;
  @NonNull RuntimeClient runtimeClient;
  @NonNull RuntimeContainerProperties containerProperties;
  @NonNull SynchronizerProperties synchronizerProperties;

  @PostConstruct
  public void init() {
    final var setStatusStarting = runtimeClient.setStatus(containerProperties.getContainerId(), ContainerStatus.STARTING);
    final var downloadFiles = Flux.fromIterable(synchronizerProperties.getFileDownloads())
        .flatMap(fileTransfer -> storageClient.downloadFile(fileTransfer.getLocalPath(), fileTransfer.getRemotePath()))
        .collectList();
    final var downloadFolders = Flux.fromIterable(synchronizerProperties.getFolderDownloads())
        .flatMap(fileTransfer -> storageClient.downloadFolder(fileTransfer.getLocalPath(), Optional.of(fileTransfer.getRemotePath())))
        .collectList();
    final var setStatusStopping = runtimeClient.setStatus(containerProperties.getContainerId(), ContainerStatus.STOPPING);
    final var waitForStatusStopping = runtimeClient.waitForStatus(containerProperties.getTaskId(), ContainerStatus.STOPPING);
    final var uploadFiles = Flux.fromIterable(synchronizerProperties.getFileUploads())
        .flatMap(fileTransfer -> storageClient.uploadFile(fileTransfer.getLocalPath(), Optional.of(fileTransfer.getRemotePath())))
        .collectList();
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
