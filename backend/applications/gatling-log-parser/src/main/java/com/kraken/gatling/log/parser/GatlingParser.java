package com.kraken.gatling.log.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.entity.DebugEntry;
import com.kraken.debug.entry.writer.DebugEntryWriter;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.predicate.TaskPredicate;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Task;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.kraken.tools.reactor.utils.ReactorUtils.waitFor;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class GatlingParser {

  @NonNull LogParser parser;
  @NonNull RuntimeClient runtimeClient;
  @NonNull DebugEntryWriter writer;
  @NonNull RuntimeContainerProperties containerProperties;
  @NonNull GatlingParserProperties gatlingProperties;
  @NonNull TaskPredicate taskPredicate;
  @NonNull CommandService commandService;

  @PostConstruct
  public void init() throws InterruptedException {
    final var findMe = runtimeClient.find(containerProperties.getTaskId(), containerProperties.getContainerName());
    final var me = findMe.block();
    final var setStatusReady = runtimeClient.setStatus(me, ContainerStatus.READY);
    final var waitForStatusReady = runtimeClient.waitForStatus(containerProperties.getTaskId(), ContainerStatus.READY);
    final var setStatusRunning = runtimeClient.setStatus(me, ContainerStatus.RUNNING);
    final var listFiles = commandService.execute(Command.builder()
        .path(gatlingProperties.getGatlingHome().toString())
        .command(ImmutableList.of("ls", "-lR"))
        .environment(ImmutableMap.of())
        .build());
    final var parse = writer.write(parser.parse(gatlingProperties.getDebugLog()));
    final var setStatusDone = runtimeClient.setStatus(me, ContainerStatus.DONE);

    setStatusReady.map(Object::toString)
        .doOnError(t -> log.error("Failed to set status READY", t))
        .doOnNext(log::info).block();
    waitForStatusReady.map(Object::toString)
        .doOnError(t -> log.error("Failed to wait for status READY", t))
        .doOnNext(log::info).block();
    setStatusRunning.map(Object::toString)
        .doOnError(t -> log.error("Failed to set status RUNNING", t))
        .doOnNext(log::info).block();
    Optional.ofNullable(listFiles
        .doOnError(t -> log.error("Failed to list files", t))
        .collectList()
        .block()).orElse(Collections.emptyList()).forEach(log::info);
    waitFor(parse.map(DebugEntry::getRequestName)
            .doOnError(t -> log.error("Failed to wait for task completion", t))
            .doOnNext(log::info)
            .onErrorContinue((throwable, o) -> log.error("Failed to parse debug entry " + o, throwable)),
        runtimeClient.waitForPredicate(taskPredicate), Duration.ofSeconds(15));
    setStatusDone.map(Object::toString)
        .doOnError(t -> log.error("Failed to set status DONE", t))
        .doOnNext(log::info).block();
  }

}
