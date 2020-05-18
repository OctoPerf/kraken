package com.kraken.parser.log;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.debug.entry.writer.DebugEntryWriter;
import com.kraken.gatling.log.parser.LogParser;
import com.kraken.config.gatling.api.GatlingProperties;
import com.kraken.runtime.client.api.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.executor.ContainerExecutor;
import com.kraken.runtime.container.predicate.TaskPredicate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

import static com.kraken.tools.reactor.utils.ReactorUtils.waitFor;
import static java.util.Optional.empty;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
final class GatlingParser {

  @NonNull RuntimeClient runtimeClient;
  @NonNull LogParser parser;
  @NonNull DebugEntryWriter writer;
  @NonNull GatlingProperties gatling;
  @NonNull TaskPredicate taskPredicate;
  @NonNull CommandService commandService;
  @NonNull ContainerExecutor executor;

  @PostConstruct
  public void init() {
    executor.execute(empty(), me -> {
      // List files
      final var listFiles = commandService.execute(Command.builder()
          .path(Paths.get(gatling.getHome()).toString())
          .command(ImmutableList.of("ls", "-lR"))
          .environment(ImmutableMap.of())
          .build());
      Optional.ofNullable(listFiles
          .collectList()
          .block()).orElse(Collections.emptyList()).forEach(log::info);

      // Parse logs
      final var parse = writer.write(parser.parse(Paths.get(gatling.getLogs().getDebug())));
      waitFor(parse.map(DebugEntry::getRequestName)
              .doOnNext(log::info)
              .onErrorContinue((throwable, o) -> log.error("Failed to parse debug entry " + o, throwable)),
          runtimeClient.waitForPredicate(me, taskPredicate), Duration.ofSeconds(15));
    }, empty());
  }

}
