package com.kraken.gatling.container.recorder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.config.gatling.api.GatlingProperties;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.properties.ContainerProperties;
import com.kraken.storage.client.StorageClient;
import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageNodeType;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Supplier;

import static com.google.common.base.Strings.nullToEmpty;
import static com.kraken.runtime.entity.task.ContainerStatus.*;
import static java.nio.file.Paths.get;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class GatlingRecorder {

  @NonNull StorageClient storage;
  @NonNull RuntimeClient runtime;
  @NonNull CommandService commands;
  @NonNull ContainerProperties container;
  @NonNull GatlingProperties gatling;
  @NonNull Supplier<Command> newCommands;

  @PostConstruct
  public void init() {
    final var findMe = runtime.find(container.getTaskId(), container.getName());
    final var me = findMe.block();
    final var setStatusFailed = runtime.setFailedStatus(me);
    final var setStatusPreparing = runtime.setStatus(me, PREPARING);
    final var downloadConfiguration = storage.downloadFolder(get(gatling.getConf().getLocal()), getRemoteConf());
    final var downloadHAR = storage.downloadFile(get(gatling.getHarPath().getLocal()), gatling.getHarPath().getRemote());
    final var setStatusReady = runtime.setStatus(me, READY);
    final var waitForStatusReady = runtime.waitForStatus(me, READY);
    final var listFiles = commands.execute(Command.builder()
      .path(gatling.getHome())
      .command(ImmutableList.of("ls", "-lR"))
      .environment(ImmutableMap.of())
      .build());
    final var setStatusRunning = runtime.setStatus(me, RUNNING);
    final var startGatling = commands.execute(newCommands.get());
    final var setStatusStopping = runtime.setStatus(me, STOPPING);
    final var waitForStatusStopping = runtime.waitForStatus(me, STOPPING);
    final var uploadSimulation = storage.uploadFile(get(gatling.getUserFiles().getLocal()), gatling.getUserFiles().getRemote());
    final var setStatusDone = runtime.setStatus(me, DONE);

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

  private String getRemoteConf() {
    return get(nullToEmpty(gatling.getConf().getRemote()))
      .resolve(container.getTaskType().toString()).toString();
  }
}
