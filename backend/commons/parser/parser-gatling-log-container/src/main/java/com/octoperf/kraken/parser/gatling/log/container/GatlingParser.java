package com.octoperf.kraken.parser.gatling.log.container;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.analysis.entity.DebugEntry;
import com.octoperf.kraken.config.gatling.api.GatlingProperties;
import com.octoperf.kraken.parser.debug.entry.writer.api.DebugEntryWriter;
import com.octoperf.kraken.parser.gatling.log.api.LogParser;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.runtime.container.executor.ContainerExecutor;
import com.octoperf.kraken.runtime.container.predicate.TaskPredicate;
import com.octoperf.kraken.tools.reactor.utils.ReactorUtils;
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

import static java.util.Optional.empty;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
final class GatlingParser {

  @NonNull LogParser parser;
  @NonNull DebugEntryWriter writer;
  @NonNull GatlingProperties gatling;
  @NonNull TaskPredicate taskPredicate;
  @NonNull CommandService commandService;
  @NonNull ContainerExecutor executor;

  @PostConstruct
  public void init() {
    executor.execute(empty(), (runtimeClient, me) -> {
      // List files
      final var listFiles = commandService.validate(Command.builder()
          .path(Paths.get(gatling.getHome()).toString())
          .args(ImmutableList.of("ls", "-lR"))
          .environment(ImmutableMap.of())
          .build()).flatMapMany(commandService::execute);
      Optional.ofNullable(listFiles
          .collectList()
          .block()).orElse(Collections.emptyList()).forEach(log::info);

      // Parse logs
      final var parse = writer.write(parser.parse(Paths.get(gatling.getLogs().getDebug())));
      ReactorUtils.waitFor(parse.map(DebugEntry::getRequestName)
              .doOnNext(log::info)
              .onErrorContinue((throwable, o) -> log.error("Failed to parse debug entry " + o, throwable)),
          runtimeClient.waitForPredicate(me, taskPredicate), Duration.ofSeconds(15));
    }, empty());
  }

}
