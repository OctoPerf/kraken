package com.kraken.runtime.command;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.LogOutputStream;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class ZtCommandService implements CommandService {

  Function<String, String> stringCleaner;
  AtomicReference<Command> lastCommand;

  @Autowired
  public ZtCommandService(final Function<String, String> stringCleaner) {
    this.stringCleaner = Objects.requireNonNull(stringCleaner);
    this.lastCommand = new AtomicReference<>(Command.builder()
        .command(ImmutableList.of())
        .path("")
        .environment(ImmutableMap.of())
        .build());
  }

  @Override
  public Flux<String> execute(final Command command) {
    return Flux.<String>create(emitter -> {
      this.logCommand(command);
      final var file = Paths.get(command.getPath()).toFile();
      final var errors = ImmutableList.<String>builder();
      final var process = new ProcessExecutor().command(command.getCommand())
          .directory(file)
          .environment(command.getEnvironment())
          .redirectErrorStream(true)
          .redirectOutput(new LogOutputStream() {
            @Override
            protected void processLine(final String line) {
              errors.add(line);
              emitter.next(line);
            }
          });
      try {
        process.execute();
      } catch (InterruptedException | TimeoutException | IOException e) {
        log.error("Command execution failed", e);
        log.error(String.join("\n", errors.build()));
        emitter.error(e);
      }
      emitter.complete();
    })
        .map(stringCleaner);
  }

  private void logCommand(final Command command) {
    final var last = this.lastCommand.getAndSet(command);
    if (!last.equals(command)) {
      log.info(String.format("Executing command %s in path %s", String.join(" ", command.getCommand()), command.getPath()));
    }
  }

}
