package com.kraken.gatling.recorder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.entity.task.ContainerStatus;
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
import java.util.Collections;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class GatlingRecorder {

  @NonNull StorageClient storage;
  @NonNull RuntimeClient runtime;
  @NonNull CommandService commands;
  @NonNull RuntimeContainerProperties container;
  @NonNull GatlingExecutionProperties gatling;
  @NonNull Supplier<Command> newCommands;

  @PostConstruct
  public void init() {
    final var findMe = runtime.find(container.getTaskId(), container.getContainerName());
    final var me = findMe.block();
    final var setStatusFailed = runtime.setFailedStatus(me);
    final var setStatusPreparing = runtime.setStatus(me, ContainerStatus.PREPARING);
    final var downloadConfiguration = storage.downloadFolder(gatling.getLocalConf(), gatling.getRemoteConf());
    final var downloadHAR = storage.downloadFile(gatling.getLocalHarPath(), gatling.getRemoteHarPath());
    final var setStatusReady = runtime.setStatus(me, ContainerStatus.READY);
    final var waitForStatusReady = runtime.waitForStatus(me, ContainerStatus.READY);
    final var listFiles = commands.execute(Command.builder()
        .path(gatling.getGatlingHome().toString())
        .command(ImmutableList.of("ls", "-lR"))
        .environment(ImmutableMap.of())
        .build());
    final var setStatusRunning = runtime.setStatus(me, ContainerStatus.RUNNING);
    final var startGatling = commands.execute(newCommands.get());
    final var setStatusStopping = runtime.setStatus(me, ContainerStatus.STOPPING);
    final var waitForStatusStopping = runtime.waitForStatus(me, ContainerStatus.STOPPING);
    final var uploadSimulation = storage.uploadFile(gatling.getLocalUserFiles(), gatling.getRemoteUserFiles());
    final var setStatusDone = runtime.setStatus(me, ContainerStatus.DONE);

    setStatusPreparing.block();
    downloadConfiguration
        .onErrorResume(throwable -> setStatusFailed.then())
        .block();
    downloadHAR
        .onErrorResume(throwable -> setStatusFailed.then())
        .block();
    setStatusReady.block();
    waitForStatusReady.map(Object::toString).block();
    Optional.ofNullable(listFiles
        .doOnError(t -> log.error("Failed to list files", t))
        .collectList()
        .onErrorResume(throwable -> setStatusFailed.map(aVoid -> ImmutableList.of()))
        .block()).orElse(Collections.emptyList()).forEach(log::info);
    setStatusRunning.block();
    startGatling
        .doOnError(t -> log.error("Failed to start gatling", t))
        .onErrorResume(throwable -> setStatusFailed.map(aVoid -> "Failed to start gatling"))
        .doOnNext(log::info).blockLast();
    setStatusStopping.block();
    waitForStatusStopping.block();
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
