package com.octoperf.kraken.runtime.command;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.LogOutputStream;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class ZtCommandService implements CommandService {

  @NonNull
  UnaryOperator<String> stringCleaner;

  @Override
  public Flux<String> execute(final Command command) {
    return Flux.<String>create(emitter -> {
      log.debug(String.format("Executing command %s in path %s", String.join(" ", command.getCommands()), command.getPath()));
      final var file = Paths.get(command.getPath()).toFile();
      final var errors = ImmutableList.<String>builder();
      final var process = new ProcessExecutor()
          .exitValueNormal()
          .command(command.getCommands())
          .directory(file)
          .environment(command.getEnvironment().entrySet().stream()
              .collect(Collectors.toMap(e -> e.getKey().name(), Map.Entry::getValue)))
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
      } catch (InvalidExitValueException | InterruptedException | TimeoutException | IOException e) {
        log.error("Command execution failed", e);
        log.error(String.join("\n", errors.build()));
        emitter.error(e);
        // Restore interrupted state...
        Thread.currentThread().interrupt();
      }
      emitter.complete();
    })
    .map(stringCleaner);
  }

}
