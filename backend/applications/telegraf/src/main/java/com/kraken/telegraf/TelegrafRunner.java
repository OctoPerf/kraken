package com.kraken.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.client.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Task;
import com.kraken.storage.client.StorageClient;
import com.kraken.tools.reactor.utils.ReactorUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.kraken.tools.reactor.utils.ReactorUtils.waitFor;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class TelegrafRunner {

  @NonNull RuntimeClient runtimeClient;
  @NonNull CommandService commandService;
  @NonNull StorageClient storageClient;
  @NonNull RuntimeContainerProperties containerProperties;
  @NonNull TelegrafProperties telegrafProperties;
  @NonNull Supplier<Command> commandSupplier;
  @NonNull Predicate<Task> taskPredicate;

  @PostConstruct
  public void init() throws InterruptedException {
    final var setStatusPreparing = runtimeClient.setStatus(containerProperties.getContainerId(), ContainerStatus.PREPARING);
    final var downloadConfFile = storageClient.downloadFile(telegrafProperties.getLocalConf(), telegrafProperties.getRemoteConf());
    final var displayTelegrafConf = commandService.execute(Command.builder()
        .path("/etc/telegraf")
        .environment(ImmutableMap.of())
        .command(ImmutableList.of("cat", "telegraf.conf"))
        .build());
    final var setStatusRunning = runtimeClient.setStatus(containerProperties.getContainerId(), ContainerStatus.RUNNING);
    final var startTelegraf = commandService.execute(commandSupplier.get()).doOnNext(log::info);
    final var setStatusDone = runtimeClient.setStatus(containerProperties.getContainerId(), ContainerStatus.DONE);

    setStatusPreparing.map(Object::toString).doOnNext(log::info).block();
    downloadConfFile.block();
    // Mandatory or the file is empty
    Optional.ofNullable(displayTelegrafConf.collectList().block()).orElse(Collections.emptyList()).forEach(log::info);
    setStatusRunning.map(Object::toString).doOnNext(log::info).block();
    waitFor(startTelegraf, runtimeClient.waitForPredicate(taskPredicate), Duration.ofSeconds(5));
    setStatusDone.map(Object::toString).doOnNext(log::info).block();
  }

}
