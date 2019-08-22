package com.kraken.commons.command.executor;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.kraken.commons.command.entity.Command;
import com.kraken.commons.command.entity.CommandLog;
import com.kraken.commons.command.entity.CommandLogStatus;
import com.kraken.commons.command.logs.LogsQueue;
import com.kraken.commons.rest.configuration.ApplicationProperties;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.LogOutputStream;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
class ZtCommandExecutor implements CommandExecutor {
  private static final int MAX_LOGS_SIZE = 500;
  private static final int MAX_LOGS_TIMEOUT_MS = 1000;
  private static final String LINE_SEP = "\r\n";

  @NonNull
  Function<String, String> stringCleaner;
  @NonNull
  Map<String, Disposable> subscriptionsMap;
  @NonNull
  LogsQueue logsQueue;
  @NonNull
  ApplicationProperties applicationProperties;

  @Override
  public Mono<String> execute(final Command command) {
    final var id = UUID.randomUUID().toString();
    final var updated = command.withId(id)
        .withEnvironment(ImmutableMap.<String, String>builder()
            .putAll(command.getEnvironment())
            .put("KRAKEN_DATA", applicationProperties.getData().toAbsolutePath().toString())
            .put("KRAKEN_HOST_DATA", applicationProperties.getHostData())
            .put("KRAKEN_HOST_UID", applicationProperties.getHostUId())
            .put("KRAKEN_HOST_GID", applicationProperties.getHostGId())
            .build());

    this.logsQueue.publish(updated, CommandLogStatus.INITIALIZED, "");

    final var subscription = Flux.<String>create(emitter -> this.run(updated.getCommand(), updated.getPath(), updated.getEnvironment(), emitter))
        .map((line) -> stringCleaner.apply(line) + LINE_SEP)
        .windowTimeout(MAX_LOGS_SIZE, Duration.ofMillis(MAX_LOGS_TIMEOUT_MS))
        .flatMap(window -> window.reduce((o, o2) -> o + o2))
        .subscribeOn(Schedulers.elastic())
        .doOnError(throwable -> this.onError(updated, throwable))
        .doOnCancel(() -> this.onCancel(updated))
        .doOnComplete(() -> this.onDispose(updated))
        .subscribe(s -> this.logsQueue.publish(updated, CommandLogStatus.RUNNING, s));

    this.subscriptionsMap.put(updated.getId(), subscription);

    return Mono.just(updated.getId());
  }

  @Override
  public Flux<CommandLog> listen(final String applicationId) {
    final var id = UUID.randomUUID().toString();
    return Flux.<CommandLog>create((listener) -> this.logsQueue.addListener(id, applicationId, listener))
        .doOnComplete(() -> this.logsQueue.removeListener(id))
        .doOnCancel(() -> this.logsQueue.removeListener(id))
        .doOnError(throwable -> {
          throwable.printStackTrace();
          this.logsQueue.removeListener(id);
        });
  }

  @Override
  public boolean cancel(String commandId) {
    if (this.subscriptionsMap.containsKey(commandId)) {
      final var subscription = this.subscriptionsMap.get(commandId);
      subscription.dispose();
      return true;
    }
    return false;
  }

  @Override
  public void clear() {
    logsQueue.clear();
    subscriptionsMap.clear();
  }

  private void run(final List<String> command,
                   final String path,
                   final Map<String, String> environment,
                   final FluxSink<String> emitter) {
    final var file = this.applicationProperties.getData().resolve(path).toFile();
    final var process = new ProcessExecutor().command(command)
        .directory(file)
        .environment(environment)
        .redirectErrorStream(true)
        .redirectOutput(new LogOutputStream() {
          @Override
          protected void processLine(final String line) {
            emitter.next(line);
          }
        });
    try {
      process.execute();
    } catch (InterruptedException | TimeoutException | IOException e) {
      emitter.error(e);
    } finally {
      emitter.complete();
    }
  }

  @VisibleForTesting
  void onError(final Command command, final Throwable throwable) {
    if (throwable instanceof InterruptedException) {
      log.warn("Command interrupted " + command, throwable);
    } else {
      log.error("An exception occurred while executing the command '" + command, throwable);
      this.logsQueue.publish(command, CommandLogStatus.RUNNING, "An exception occurred while executing the command '" +
          command + "'\n\n" + Throwables.getStackTraceAsString(throwable));
    }
    this.onDispose(command);
  }

  private void onCancel(final Command command) {
    log.warn("Cancel command "+ command);
    if (!command.getOnCancel().isEmpty()) {
      this.subscriptionsMap.remove(command.getId());

      this.logsQueue.publish(command, CommandLogStatus.CANCELLING, "");

      final var subscription = Flux.<String>create(emitter -> this.run(command.getOnCancel(), command.getPath(), command.getEnvironment(), emitter))
          .map((line) -> stringCleaner.apply(line) + LINE_SEP)
          .windowTimeout(MAX_LOGS_SIZE / 10, Duration.ofMillis(MAX_LOGS_TIMEOUT_MS / 10))
          .flatMap(window -> window.reduce((o, o2) -> o + o2))
          .subscribeOn(Schedulers.elastic())
          .doOnError(throwable -> this.onError(command, throwable))
          .doOnCancel(() -> this.onDispose(command))
          .doOnComplete(() -> this.onDispose(command))
          .subscribe(s -> this.logsQueue.publish(command, CommandLogStatus.CANCELLING, s));

      this.subscriptionsMap.put(command.getId(), subscription);
    } else {
      this.onDispose(command);
    }
  }

  private void onDispose(final Command command) {
    this.subscriptionsMap.remove(command.getId());
    this.logsQueue.publish(command, CommandLogStatus.CLOSED, "");
  }

}
