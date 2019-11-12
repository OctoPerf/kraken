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
    final var setStatusPreparing = runtimeClient.setStatus(containerProperties.getTaskId(), containerProperties.getHostId(), containerProperties.getContainerId(), ContainerStatus.PREPARING);
    final var downloadConfiguration = storageClient.downloadFolder(gatlingExecutionProperties.getLocalConf(), gatlingExecutionProperties.getRemoteConf());
    final var downloadHAR = storageClient.downloadFile(gatlingExecutionProperties.getLocalHarPath(), gatlingExecutionProperties.getRemoteHarPath());
    final var setStatusReady = runtimeClient.setStatus(containerProperties.getTaskId(), containerProperties.getHostId(), containerProperties.getContainerId(), ContainerStatus.READY);
    final var waitForStatusReady = runtimeClient.waitForStatus(containerProperties.getTaskId(), ContainerStatus.READY);
    final var listFiles = commandService.execute(Command.builder()
        .path(gatlingExecutionProperties.getGatlingHome().toString())
        .command(ImmutableList.of("ls", "-lR"))
        .environment(ImmutableMap.of())
        .build());
    final var setStatusRunning = runtimeClient.setStatus(containerProperties.getTaskId(), containerProperties.getHostId(), containerProperties.getContainerId(), ContainerStatus.RUNNING);
    final var startGatling = commandService.execute(commandSupplier.get());
    final var setStatusStopping = runtimeClient.setStatus(containerProperties.getTaskId(), containerProperties.getHostId(), containerProperties.getContainerId(), ContainerStatus.STOPPING);
    final var waitForStatusStopping = runtimeClient.waitForStatus(containerProperties.getTaskId(), ContainerStatus.STOPPING);
    final var uploadSimulation = storageClient.uploadFile(gatlingExecutionProperties.getLocalUserFiles(), gatlingExecutionProperties.getRemoteUserFiles());
    final var setStatusDone = runtimeClient.setStatus(containerProperties.getTaskId(), containerProperties.getHostId(), containerProperties.getContainerId(), ContainerStatus.DONE);

    setStatusPreparing.map(Object::toString).doOnNext(log::info).block();
    downloadConfiguration.block();
    downloadHAR.block();
    setStatusReady.map(Object::toString).doOnNext(log::info).block();
    waitForStatusReady.map(Object::toString).doOnNext(log::info).block();
    listFiles.doOnNext(log::debug).blockLast();
    setStatusRunning.map(Object::toString).doOnNext(log::info).block();
    startGatling.doOnNext(log::info).blockLast();
    setStatusStopping.map(Object::toString).doOnNext(log::info).block();
    waitForStatusStopping.map(Object::toString).doOnNext(log::info).block();
    uploadSimulation.block();
    setStatusDone.map(Object::toString).doOnNext(log::info).block();
  }

}
