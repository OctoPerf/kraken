package com.kraken.parser.har;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.config.har.parser.api.HarParserProperties;
import com.kraken.debug.entry.writer.DebugEntryWriter;
import com.kraken.har.parser.HarParser;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.properties.ContainerProperties;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.storage.client.StorageClient;
import com.kraken.config.api.ApplicationProperties;
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

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class HarParserService {

  @NonNull HarParser parser;
  @NonNull RuntimeClient runtime;
  @NonNull StorageClient storage;
  @NonNull CommandService commands;
  @NonNull DebugEntryWriter writer;
  @NonNull ContainerProperties container;
  @NonNull HarParserProperties harParser;
  @NonNull ApplicationProperties application;

  @PostConstruct
  public void init() {
    final var findMe = runtime.find(container.getTaskId(), container.getName());
    final var me = findMe.block();
    final var setStatusFailed = runtime.setFailedStatus(me);
    final var setStatusPreparing = runtime.setStatus(me, ContainerStatus.PREPARING);
    final var localFolderPath = Paths.get(harParser.getLocal());
    final var downloadHAR = storage.downloadFile(localFolderPath, harParser.getRemote());
    final var setStatusReady = runtime.setStatus(me, ContainerStatus.READY);
    final var waitForStatusReady = runtime.waitForStatus(me, ContainerStatus.READY);
    final var setStatusRunning = runtime.setStatus(me, ContainerStatus.RUNNING);
    final var listFiles = commands.execute(Command.builder()
        .path(application.getData())
        .command(ImmutableList.of("ls", "-lR"))
        .environment(ImmutableMap.of())
        .build());
    final var parse = writer.write(parser.parse(localFolderPath));
    final var setStatusDone = runtime.setStatus(me, ContainerStatus.DONE);

    setStatusPreparing.block();
    downloadHAR.block();
    Optional.ofNullable(listFiles
        .doOnError(t -> log.error("Failed list files", t))
        .collectList()
        .onErrorResume(throwable -> setStatusFailed.map(aVoid -> ImmutableList.of()))
        .block()).orElse(Collections.emptyList()).forEach(log::info);
    setStatusReady.block();
    waitForStatusReady.block();
    setStatusRunning.block();
    log.info("Parsing START " + localFolderPath);
    parse.map(DebugEntry::getRequestName)
        .doOnError(t -> log.error("Failed to parse debug entry", t))
        .doOnNext(log::info).collectList().block();
    log.info("Parsing END");
    setStatusDone.block();
  }

}
