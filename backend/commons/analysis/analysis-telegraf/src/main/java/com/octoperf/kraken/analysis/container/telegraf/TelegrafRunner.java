package com.octoperf.kraken.analysis.container.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.config.telegraf.api.TelegrafProperties;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.runtime.container.executor.ContainerExecutor;
import com.octoperf.kraken.runtime.container.predicate.TaskPredicate;
import com.octoperf.kraken.storage.client.api.StorageClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.function.Supplier;

import static com.octoperf.kraken.tools.reactor.utils.ReactorUtils.waitFor;
import static java.nio.file.Paths.get;
import static java.time.Duration.ofSeconds;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static lombok.AccessLevel.PACKAGE;

@Slf4j
@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class TelegrafRunner {

  @NonNull CommandService commands;
  @NonNull Mono<StorageClient> storageClientMono;
  @NonNull TelegrafProperties telegraf;
  @NonNull Supplier<Command> newCommand;
  @NonNull TaskPredicate tasks;
  @NonNull ContainerExecutor executor;

  @PostConstruct
  public void init() {
    executor.execute(of((runtimeClient, me) -> {
      // Download configuration file
      storageClientMono.flatMap(client -> client.downloadFile(get(telegraf.getLocal()), telegraf.getRemote())).block();
      // Mandatory or the file is empty
      final var displayTelegrafConf = commands.validate(Command.builder()
          .path("/etc/telegraf")
          .environment(ImmutableMap.of())
          .args(ImmutableList.of("cat", "telegraf.conf"))
          .build()).flatMapMany(commands::execute);
      Optional.ofNullable(displayTelegrafConf.collectList()
          .block()).orElse(emptyList()).forEach(log::info);
    }), (runtimeClient, me) -> {
      final var startTelegraf = commands.validate(newCommand.get()).flatMapMany(commands::execute);
      waitFor(startTelegraf
          .doOnNext(log::info), runtimeClient.waitForPredicate(me, tasks), ofSeconds(5));
    }, empty());
  }

}
