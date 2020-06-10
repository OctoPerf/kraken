package com.octoperf.kraken.gatling.container.recorder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.config.gatling.api.GatlingProperties;
import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.runtime.command.Command;
import com.octoperf.kraken.runtime.command.CommandService;
import com.octoperf.kraken.runtime.container.executor.ContainerExecutor;
import com.octoperf.kraken.storage.client.api.StorageClient;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Supplier;

import static com.google.common.base.Strings.nullToEmpty;
import static java.nio.file.Paths.get;
import static java.util.Optional.of;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class GatlingRecorder {

  @NonNull Mono<StorageClient> storageClientMono;
  @NonNull CommandService commands;
  @NonNull ContainerProperties container;
  @NonNull GatlingProperties gatling;
  @NonNull Supplier<Command> newCommands;
  @NonNull ContainerExecutor executor;

  @PostConstruct
  public void init() {
    executor.execute(of((runtimeClient, me) -> {
      storageClientMono.flatMap(storage ->
          Mono.zip(storage.downloadFolder(Paths.get(gatling.getConf().getLocal()), getRemoteConf()),
              storage.downloadFile(Paths.get(gatling.getHarPath().getLocal()), gatling.getHarPath().getRemote()))
      ).block();
      // List files
      final var listFiles = commands.execute(Command.builder()
          .path(gatling.getHome())
          .command(ImmutableList.of("ls", "-lR"))
          .environment(ImmutableMap.of())
          .build());
      Optional.ofNullable(listFiles
          .collectList()
          .block()).orElse(Collections.emptyList()).forEach(log::info);
    }), (runtimeClient, me) -> {
      // Start gatling
      commands.execute(newCommands.get())
          .doOnNext(log::info).blockLast();
    }, of((runtimeClient, me) -> {
      // Upload simulation
      storageClientMono.flatMap(storage ->
          storage.uploadFile(Paths.get(gatling.getUserFiles().getLocal()), gatling.getUserFiles().getRemote()))
          .block();
    }));
  }

  private String getRemoteConf() {
    return get(nullToEmpty(gatling.getConf().getRemote()))
        .resolve(container.getTaskType().toString()).toString();
  }
}
