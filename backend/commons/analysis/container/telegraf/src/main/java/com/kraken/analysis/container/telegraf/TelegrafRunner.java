package com.kraken.analysis.container.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.config.telegraf.api.TelegrafProperties;
import com.kraken.runtime.client.api.RuntimeClient;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.executor.ContainerExecutor;
import com.kraken.runtime.container.predicate.TaskPredicate;
import com.kraken.storage.client.api.StorageClient;
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
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static lombok.AccessLevel.PACKAGE;

@Slf4j
@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class TelegrafRunner {

  @NonNull RuntimeClient client;
  @NonNull CommandService commands;
  @NonNull StorageClient storage;
  @NonNull TelegrafProperties telegraf;
  @NonNull Supplier<Command> newCommand;
  @NonNull TaskPredicate tasks;
  @NonNull ContainerExecutor executor;

  @PostConstruct
  public void init() {
    executor.execute(of(me -> {
      // Download configuration file
      storage.downloadFile(get(telegraf.getLocal()), telegraf.getRemote()).block();
      // Mandatory or the file is empty
      final var displayTelegrafConf = commands.execute(Command.builder()
          .path("/etc/telegraf")
          .environment(ImmutableMap.of())
          .command(ImmutableList.of("cat", "telegraf.conf"))
          .build());
      Optional.ofNullable(displayTelegrafConf.collectList()
          .block()).orElse(emptyList()).forEach(log::info);
    }), me -> {
      final var startTelegraf = commands.execute(newCommand.get());
      waitFor(startTelegraf
          .doOnNext(log::info), client.waitForPredicate(me, tasks), ofSeconds(5));
    }, empty());
  }

}
