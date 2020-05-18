package com.kraken.parser.har;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.config.api.ApplicationProperties;
import com.kraken.config.har.parser.api.HarParserProperties;
import com.kraken.debug.entry.writer.DebugEntryWriter;
import com.kraken.har.parser.HarParser;
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
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
final class HarParserService {

  @NonNull HarParser parser;
  @NonNull StorageClient storage;
  @NonNull CommandService commands;
  @NonNull DebugEntryWriter writer;
  @NonNull HarParserProperties harParser;
  @NonNull ApplicationProperties application;
  @NonNull ContainerExecutor executor;

  @PostConstruct
  public void init() {
    final var localFolderPath = Paths.get(harParser.getLocal());
    executor.execute(of(me -> {
      // Download HAR
      storage.downloadFile(localFolderPath, harParser.getRemote()).block();
      // List files
      final var listFiles = commands.execute(Command.builder()
          .path(application.getData())
          .command(ImmutableList.of("ls", "-lR"))
          .environment(ImmutableMap.of())
          .build());
      Optional.ofNullable(listFiles
          .collectList()
          .block()).orElse(Collections.emptyList()).forEach(log::info);
    }), me -> {
      log.info("Parsing START " + localFolderPath);
      // Parser HAR
      final var parse = writer.write(parser.parse(localFolderPath));
      parse.map(DebugEntry::getRequestName)
          .doOnError(t -> log.error("Failed to parse debug entry", t))
          .doOnNext(log::info).collectList().block();
      log.info("Parsing END");
    }, empty());
  }

}
