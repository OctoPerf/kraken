package com.kraken.gatling.container.runner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.config.gatling.api.GatlingProperties;
import com.kraken.config.runtime.container.api.ContainerProperties;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.executor.ContainerExecutor;
import com.kraken.storage.client.api.StorageClient;
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
import static java.util.Optional.of;
import static lombok.AccessLevel.PACKAGE;

@Slf4j
@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class GatlingRunner {

  @NonNull StorageClient storage;
  @NonNull CommandService commands;
  @NonNull ContainerProperties container;
  @NonNull GatlingProperties gatling;
  @NonNull Supplier<Command> newCommand;
  @NonNull ContainerExecutor executor;

  @PostConstruct
  public void init() {
    executor.execute(of(me -> {
      // Download configuration
      storage.downloadFolder(get(gatling.getConf().getLocal()), getRemoteConf()).block();
      // Download user files
      storage.downloadFolder(get(gatling.getUserFiles().getLocal()), gatling.getUserFiles().getRemote()).block();
      // Download lib folder
      storage.downloadFolder(get(gatling.getLib().getLocal()), gatling.getLib().getRemote()).block();
      // List files
      final var listFiles = commands.execute(Command.builder()
          .path(gatling.getHome())
          .command(ImmutableList.of("ls", "-lR"))
          .environment(ImmutableMap.of())
          .build());
      Optional.ofNullable(listFiles
          .collectList()
          .block()).orElse(Collections.emptyList())
          .forEach(log::info);
    }), me -> {
      // Start gatling
      commands.execute(newCommand.get())
          .doOnNext(log::info).blockLast();
    }, of(me -> {
      // Upload result
      storage.uploadFile(
          get(gatling.getResults().getLocal()),
          get(getRemoteResult()).resolve("groups").resolve(container.getHostId()).toString()
      ).block();
    }));
  }

  private String getRemoteResult() {
    return get(nullToEmpty(gatling.getResults().getRemote())).resolve(container.getTaskId()).toString();
  }

  private String getRemoteConf() {
    return get(nullToEmpty(gatling.getConf().getRemote())).resolve(container.getTaskType().toString()).toString();
  }
}
