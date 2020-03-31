package com.kraken.runtime.command;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.LogOutputStream;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class ZtCommandService implements CommandService {

  @NonNull
  Function<String, String> stringCleaner;

  @Override
  public Flux<String> execute(final Command command) {
    return Flux.<String>create(emitter -> {
      log.debug(String.format("Executing command %s in path %s", String.join(" ", command.getCommand()), command.getPath()));
      final var file = Paths.get(command.getPath()).toFile();
      final var errors = ImmutableList.<String>builder();
      final var process = new ProcessExecutor().command(command.getCommand())
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
        final var exitValue = process.execute().getExitValue();
        if (exitValue != 0) {
          emitter.error(new IllegalArgumentException("Command returned with code != 0"));
        } else {
          emitter.complete();
        }
      } catch (InterruptedException | TimeoutException | IOException e) {
        log.error("Command execution failed", e);
        log.error(String.join("\n", errors.build()));
        emitter.error(e);
      }
    })
        .map(stringCleaner);
  }

}
