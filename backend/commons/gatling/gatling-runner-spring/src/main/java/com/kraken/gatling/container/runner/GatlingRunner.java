package com.kraken.gatling.container.runner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.config.gatling.api.GatlingProperties;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.properties.ContainerProperties;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.storage.client.StorageClient;
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

import static com.google.common.base.Strings.nullToEmpty;
import static java.nio.file.Paths.get;
import static lombok.AccessLevel.PACKAGE;

@Slf4j
@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class GatlingRunner {

  @NonNull StorageClient storage;
  @NonNull RuntimeClient runtime;
  @NonNull CommandService commands;
  @NonNull ContainerProperties container;
  @NonNull GatlingProperties gatling;
  @NonNull Supplier<Command> newCommand;

  @PostConstruct
  public void init() {
    final var findMe = runtime.find(container.getTaskId(), container.getName());
    final var me = findMe.block();
    final var setStatusFailed = runtime.setFailedStatus(me);
    final var setStatusPreparing = runtime.setStatus(me, ContainerStatus.PREPARING);
    final var downloadConfiguration = storage.downloadFolder(get(gatling.getConf().getLocal()), getRemoteConf());
    final var downloadUserFiles = storage.downloadFolder(get(gatling.getUserFiles().getLocal()), gatling.getUserFiles().getRemote());
    final var downloadLib = storage.downloadFolder(get(gatling.getLib().getLocal()), gatling.getLib().getRemote());
    final var setStatusReady = runtime.setStatus(me, ContainerStatus.READY);
    final var waitForStatusReady = runtime.waitForStatus(me, ContainerStatus.READY);
    final var listFiles = commands.execute(Command.builder()
        .path(gatling.getHome())
        .command(ImmutableList.of("ls", "-lR"))
        .environment(ImmutableMap.of())
        .build());
    final var setStatusRunning = runtime.setStatus(me, ContainerStatus.RUNNING);
    final var startGatling = commands.execute(newCommand.get());
    final var setStatusStopping = runtime.setStatus(me, ContainerStatus.STOPPING);
    final var waitForStatusStopping = runtime.waitForStatus(me, ContainerStatus.STOPPING);
    final var uploadResult = storage.uploadFile(
        get(gatling.getResults().getLocal()),
      get(getRemoteResult()).resolve("groups").resolve(container.getHostId()).toString()
    );
    final var setStatusDone = runtime.setStatus(me, ContainerStatus.DONE);

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

  private String getRemoteResult() {
    return get(nullToEmpty(gatling.getResults().getRemote())).resolve(container.getTaskId()).toString();
  }

  private String getRemoteConf() {
    return get(nullToEmpty(gatling.getConf().getRemote())).resolve(container.getTaskType().toString()).toString();
  }
}
