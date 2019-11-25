package com.kraken.har.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.analysis.entity.HttpHeader;
import com.kraken.debug.entry.writer.DebugEntryWriter;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.storage.client.StorageClient;
import com.kraken.tools.configuration.properties.ApplicationProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

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
    final var setStatusPreparing = runtimeClient.setStatus(me, ContainerStatus.PREPARING);
    final var downloadHAR = storageClient.downloadFile(parserProperties.getLocalHarPath(), parserProperties.getRemoteHarPath());
    final var setStatusReady = runtimeClient.setStatus(me, ContainerStatus.READY);
    final var waitForStatusReady = runtimeClient.waitForStatus(containerProperties.getTaskId(), ContainerStatus.READY);
    final var setStatusRunning = runtimeClient.setStatus(me, ContainerStatus.RUNNING);
    final var listFiles = commandService.execute(Command.builder()
        .path(applicationProperties.getData().toString())
        .command(ImmutableList.of("ls", "-lR"))
        .environment(ImmutableMap.of())
        .build());
    final var parse = writer.write(parser.parse(parserProperties.getLocalHarPath()));
    final var setStatusDone = runtimeClient.setStatus(me, ContainerStatus.DONE);

    setStatusPreparing.map(Object::toString).doOnNext(log::info).block();
    downloadHAR.block();
    Optional.ofNullable(listFiles.collectList().block()).orElse(Collections.emptyList()).forEach(log::info);
    setStatusReady.map(Object::toString).doOnNext(log::info).block();
    waitForStatusReady.map(Object::toString).doOnNext(log::info).block();
    setStatusRunning.map(Object::toString).doOnNext(log::info).block();
    log.info("Parsing START " + parserProperties.getLocalHarPath());
    parse.map(DebugEntry::getRequestName).doOnNext(log::info).collectList().block();
    log.info("Parsing END");
    setStatusDone.map(Object::toString).doOnNext(log::info).block();
  }

}
