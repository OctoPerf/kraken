package com.kraken.gatling.recorder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.gatling.GatlingExecutionProperties;
import com.kraken.storage.client.StorageClient;
import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageNodeType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.function.Supplier;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class GatlingRecorder {

  @NonNull StorageClient storageClient;
  @NonNull RuntimeClient runtimeClient;
  @NonNull CommandService commandService;
  @NonNull RuntimeContainerProperties containerProperties;
  @NonNull GatlingExecutionProperties gatlingExecutionProperties;
  @NonNull Supplier<Command> commandSupplier;

  @PostConstruct
  public void init() {
    final var findMe = runtimeClient.find(containerProperties.getTaskId(), containerProperties.getContainerName());
    final var me = findMe.block();
    final var setStatusFailed = runtimeClient.setFailedStatus(me);
    final var setStatusPreparing = runtimeClient.setStatus(me, ContainerStatus.PREPARING);
    final var downloadConfiguration = storageClient.downloadFolder(gatlingExecutionProperties.getLocalConf(), gatlingExecutionProperties.getRemoteConf());
    final var downloadHAR = storageClient.downloadFile(gatlingExecutionProperties.getLocalHarPath(), gatlingExecutionProperties.getRemoteHarPath());
    final var setStatusReady = runtimeClient.setStatus(me, ContainerStatus.READY);
    final var waitForStatusReady = runtimeClient.waitForStatus(me, ContainerStatus.READY);
    final var listFiles = commandService.execute(Command.builder()
        .path(gatlingExecutionProperties.getGatlingHome().toString())
        .command(ImmutableList.of("ls", "-lR"))
        .environment(ImmutableMap.of())
        .build());
    final var setStatusRunning = runtimeClient.setStatus(me, ContainerStatus.RUNNING);
    final var startGatling = commandService.execute(commandSupplier.get());
    final var setStatusStopping = runtimeClient.setStatus(me, ContainerStatus.STOPPING);
    final var waitForStatusStopping = runtimeClient.waitForStatus(me, ContainerStatus.STOPPING);
    final var uploadSimulation = storageClient.uploadFile(gatlingExecutionProperties.getLocalUserFiles(), gatlingExecutionProperties.getRemoteUserFiles());
    final var setStatusDone = runtimeClient.setStatus(me, ContainerStatus.DONE);

    setStatusPreparing.map(Object::toString)
        .doOnNext(log::info).block();
    downloadConfiguration
        .doOnError(t -> log.error("Failed to download configuration", t))
        .onErrorResume(throwable -> setStatusFailed.then())
        .block();
    downloadHAR
        .doOnError(t -> log.error("Failed to download HAR", t))
        .onErrorResume(throwable -> setStatusFailed.then())
        .block();
    setStatusReady.map(Object::toString)
        .doOnError(t -> log.error("Failed to set status READY", t))
        .doOnNext(log::info).block();
    waitForStatusReady.map(Object::toString)
        .doOnError(t -> log.error("Failed to wait for status READY", t))
        .doOnNext(log::info).block();
    listFiles
        .doOnError(t -> log.error("Failed to list files", t))
        .onErrorResume(throwable -> setStatusFailed.map(aVoid -> "Failed to list files"))
        .doOnNext(log::debug).blockLast();
    setStatusRunning.map(Object::toString)
        .doOnError(t -> log.error("Failed to set status RUNNING", t))
        .doOnNext(log::info).block();
    startGatling
        .doOnError(t -> log.error("Failed to start gatling", t))
        .onErrorResume(throwable -> setStatusFailed.map(aVoid -> "Failed to start gatling"))
        .doOnNext(log::info).blockLast();
    setStatusStopping.map(Object::toString)

        .doOnNext(log::info).block();
    waitForStatusStopping.map(Object::toString)
        .doOnError(t -> log.error("Failed to wait for status STOPPING", t))
        .doOnNext(log::info).block();
    uploadSimulation
        .doOnError(t -> log.error("Failed to upload simulation", t))
        .onErrorResume(throwable -> setStatusFailed.map(s -> StorageNode.builder()
            .path("Failed")
            .depth(0)
            .lastModified(0L)
            .length(0L)
            .type(StorageNodeType.NONE)
            .build()))
        .block();
    setStatusDone.map(Object::toString)
        .doOnError(t -> log.error("Failed to set status DONE", t))
        .doOnNext(log::info).block();
  }

}
