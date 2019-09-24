package com.kraken.telegraf;

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
    final var setStatusRunning = runtimeClient.setStatus(containerProperties.getContainerId(), ContainerStatus.RUNNING);
    final var startTelegraf = commandService.execute(commandSupplier.get()).doOnNext(log::info);
    final var setStatusDone = runtimeClient.setStatus(containerProperties.getContainerId(), ContainerStatus.DONE);

    setStatusPreparing.map(Object::toString).subscribe(log::info);
    downloadConfFile.subscribe();
    setStatusRunning.map(Object::toString).subscribe(log::info);
    waitFor(startTelegraf, runtimeClient.waitForPredicate(taskPredicate), Duration.ofSeconds(5));
    setStatusDone.map(Object::toString).subscribe(log::info);
  }

}
