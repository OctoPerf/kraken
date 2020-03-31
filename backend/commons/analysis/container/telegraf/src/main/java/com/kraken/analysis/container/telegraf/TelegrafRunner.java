package com.kraken.analysis.container.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.config.telegraf.api.TelegrafProperties;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.predicate.TaskPredicate;
import com.kraken.runtime.container.properties.ContainerProperties;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.storage.client.StorageClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.function.Supplier;

import static com.kraken.tools.reactor.utils.ReactorUtils.waitFor;
import static java.nio.file.Paths.get;
import static java.time.Duration.ofSeconds;
import static java.util.Collections.emptyList;
import static lombok.AccessLevel.PACKAGE;

@Slf4j
@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class TelegrafRunner {

  @NonNull RuntimeClient client;
  @NonNull CommandService commands;
  @NonNull StorageClient storage;
  @NonNull ContainerProperties container;
  @NonNull TelegrafProperties telegraf;
  @NonNull Supplier<Command> newCommand;
  @NonNull TaskPredicate tasks;

  @PostConstruct
  public void init() throws InterruptedException {
    final var findMe = client.find(container.getTaskId(), container.getName());
    final var me = findMe.block();
    final var setStatusFailed = client.setFailedStatus(me);
    final var setStatusPreparing = client.setStatus(me, ContainerStatus.PREPARING);
    final var downloadConfFile = storage.downloadFile(get(telegraf.getLocal()), telegraf.getRemote());
    final var displayTelegrafConf = commands.execute(Command.builder()
        .path("/etc/telegraf")
        .environment(ImmutableMap.of())
        .command(ImmutableList.of("cat", "telegraf.conf"))
        .build());
    final var setStatusRunning = client.setStatus(me, ContainerStatus.RUNNING);
    final var startTelegraf = commands.execute(newCommand.get());
    final var setStatusDone = client.setStatus(me, ContainerStatus.DONE);

    setStatusPreparing.block();
    downloadConfFile.block();
    // Mandatory or the file is empty
    Optional.ofNullable(displayTelegrafConf.collectList()
        .onErrorResume(throwable -> setStatusFailed.map(aVoid -> ImmutableList.of()))
        .block()).orElse(emptyList()).forEach(log::info);
    setStatusRunning.block();
    waitFor(startTelegraf
        .onErrorResume(throwable -> setStatusFailed.map(aVoid -> "Telegraf start failed"))
        .doOnNext(log::info), client.waitForPredicate(me, tasks), ofSeconds(5));
    setStatusDone.block();
  }

}
