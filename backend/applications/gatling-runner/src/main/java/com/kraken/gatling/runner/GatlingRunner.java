package com.kraken.gatling.runner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.gatling.GatlingExecutionProperties;
import com.kraken.storage.client.StorageClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class GatlingRunner {

  @NonNull StorageClient storageClient;
  @NonNull RuntimeClient runtimeClient;
  @NonNull CommandService commandService;
  @NonNull RuntimeContainerProperties container;
  @NonNull GatlingExecutionProperties gatling;
  @NonNull Supplier<Command> commandSupplier;

  @PostConstruct
  public void init() {
    final var findMe = runtimeClient.find(container.getTaskId(), container.getContainerName());
    final var me = findMe.block();
    final var setStatusFailed = runtimeClient.setFailedStatus(me);
    final var setStatusPreparing = runtimeClient.setStatus(me, ContainerStatus.PREPARING);
    final var downloadConfiguration = storageClient.downloadFolder(gatling.getLocalConf(), gatling.getRemoteConf());
    final var downloadUserFiles = storageClient.downloadFolder(gatling.getLocalUserFiles(), gatling.getRemoteUserFiles());
    final var downloadLib = storageClient.downloadFolder(gatling.getLocalLib(), gatling.getRemoteLib());
    final var setStatusReady = runtimeClient.setStatus(me, ContainerStatus.READY);
    final var waitForStatusReady = runtimeClient.waitForStatus(me, ContainerStatus.READY);
    final var listFiles = commandService.execute(Command.builder()
        .path(gatling.getGatlingHome().toString())
        .command(ImmutableList.of("ls", "-lR"))
        .environment(ImmutableMap.of())
        .build());
    final var setStatusRunning = runtimeClient.setStatus(me, ContainerStatus.RUNNING);
    final var startGatling = commandService.execute(commandSupplier.get());
    final var setStatusStopping = runtimeClient.setStatus(me, ContainerStatus.STOPPING);
    final var waitForStatusStopping = runtimeClient.waitForStatus(me, ContainerStatus.STOPPING);
    final var uploadResult = storageClient.uploadFile(
        gatling.getLocalResult(),
      Paths.get(gatling.getRemoteResult()).resolve("groups").resolve(container.getHostId()).toString()
    );
    final var setStatusDone = runtimeClient.setStatus(me, ContainerStatus.DONE);

    setStatusPreparing.block();
    downloadConfiguration.block();
    downloadUserFiles.block();
    downloadLib.block();
    setStatusReady.block();
    waitForStatusReady.map(Object::toString).block();
    Optional.ofNullable(listFiles
        .doOnError(t -> log.error("Failed to list files", t))
        .collectList()
        .onErrorResume(throwable -> setStatusFailed.map(aVoid -> ImmutableList.of()))
        .block()).orElse(Collections.emptyList())
        .forEach(log::info);
    setStatusRunning.block();
    startGatling
        .doOnError(t -> log.error("Failed to start gatling", t))
        .onErrorResume(throwable -> setStatusFailed.map(aVoid -> "Failed to start gatling"))
        .doOnNext(log::info).blockLast();
    setStatusStopping.block();
    waitForStatusStopping.block();
    uploadResult.block();
    setStatusDone.block();
  }

}
