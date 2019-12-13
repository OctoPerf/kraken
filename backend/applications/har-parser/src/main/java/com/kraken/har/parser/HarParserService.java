package com.kraken.har.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.debug.entry.writer.DebugEntryWriter;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.storage.client.StorageClient;
import com.kraken.tools.properties.ApplicationProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class HarParserService {

  @NonNull HarParser parser;
  @NonNull RuntimeClient runtimeClient;
  @NonNull StorageClient storageClient;
  @NonNull CommandService commandService;
  @NonNull DebugEntryWriter writer;
  @NonNull RuntimeContainerProperties containerProperties;
  @NonNull HarParserProperties parserProperties;
  @NonNull ApplicationProperties applicationProperties;

  @PostConstruct
  public void init() {
    final var findMe = runtimeClient.find(containerProperties.getTaskId(), containerProperties.getContainerName());
    final var me = findMe.block();
    final var setStatusFailed = runtimeClient.setFailedStatus(me);
    final var setStatusPreparing = runtimeClient.setStatus(me, ContainerStatus.PREPARING);
    final var downloadHAR = storageClient.downloadFile(parserProperties.getLocalHarPath(), parserProperties.getRemoteHarPath());
    final var setStatusReady = runtimeClient.setStatus(me, ContainerStatus.READY);
    final var waitForStatusReady = runtimeClient.waitForStatus(me, ContainerStatus.READY);
    final var setStatusRunning = runtimeClient.setStatus(me, ContainerStatus.RUNNING);
    final var listFiles = commandService.execute(Command.builder()
        .path(applicationProperties.getData().toString())
        .command(ImmutableList.of("ls", "-lR"))
        .environment(ImmutableMap.of())
        .build());
    final var parse = writer.write(parser.parse(parserProperties.getLocalHarPath()));
    final var setStatusDone = runtimeClient.setStatus(me, ContainerStatus.DONE);

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
    log.info("Parsing START " + parserProperties.getLocalHarPath());
    parse.map(DebugEntry::getRequestName)
        .doOnError(t -> log.error("Failed to parse debug entry", t))
        .doOnNext(log::info).collectList().block();
    log.info("Parsing END");
    setStatusDone.block();
  }

}
