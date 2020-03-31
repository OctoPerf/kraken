package com.kraken.parser.log;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.debug.entry.writer.DebugEntryWriter;
import com.kraken.gatling.log.parser.LogParser;
import com.kraken.config.gatling.api.GatlingProperties;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.predicate.TaskPredicate;
import com.kraken.runtime.container.properties.ContainerProperties;
import com.kraken.runtime.entity.task.ContainerStatus;
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

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class GatlingParser {

  @NonNull LogParser parser;
  @NonNull RuntimeClient runtimeClient;
  @NonNull DebugEntryWriter writer;
  @NonNull ContainerProperties container;
  @NonNull GatlingProperties gatling;
  @NonNull TaskPredicate taskPredicate;
  @NonNull CommandService commandService;

  @PostConstruct
  public void init() throws InterruptedException {
    final var findMe = runtimeClient.find(container.getTaskId(), container.getName());
    final var me = findMe.block();
    final var setStatusFailed = runtimeClient.setFailedStatus(me);
    final var setStatusReady = runtimeClient.setStatus(me, ContainerStatus.READY);
    final var waitForStatusReady = runtimeClient.waitForStatus(me, ContainerStatus.READY);
    final var setStatusRunning = runtimeClient.setStatus(me, ContainerStatus.RUNNING);
    final var listFiles = commandService.execute(Command.builder()
        .path(Paths.get(gatling.getHome()).toString())
        .command(ImmutableList.of("ls", "-lR"))
        .environment(ImmutableMap.of())
        .build());
    final var parse = writer.write(parser.parse(Paths.get(gatling.getLogs().getDebug())));
    final var setStatusDone = runtimeClient.setStatus(me, ContainerStatus.DONE);

    setStatusReady.block();
    waitForStatusReady.block();
    setStatusRunning.block();
    Optional.ofNullable(listFiles
        .doOnError(t -> log.error("Failed to list files", t))
        .collectList()
        .onErrorResume(throwable -> setStatusFailed.map(aVoid -> ImmutableList.of()))
        .block()).orElse(Collections.emptyList()).forEach(log::info);
    waitFor(parse.map(DebugEntry::getRequestName)
            .doOnNext(log::info)
            .onErrorContinue((throwable, o) -> log.error("Failed to parse debug entry " + o, throwable)),
        runtimeClient.waitForPredicate(me, taskPredicate), Duration.ofSeconds(15));
    setStatusDone.block();
  }

}
