package com.kraken.runtime.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.LogOutputStream;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class ZtCommandService implements CommandService {
  private static final int MAX_LOGS_SIZE = 500;
  private static final int MAX_LOGS_TIMEOUT_MS = 1000;
  private static final String LINE_SEP = "\r\n";

  @NonNull
  Function<String, String> stringCleaner;

  @Override
  public Flux<String> execute(final Command command) {
    return Flux.<String>create(emitter -> {
      final var file = Paths.get(command.getPath()).toFile();
      final var process = new ProcessExecutor().command(command.getCommand())
          .directory(file)
          .environment(command.getEnvironment())
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
      }
      emitter.complete();
    })
        .map((line) -> stringCleaner.apply(line) + LINE_SEP)
        .windowTimeout(MAX_LOGS_SIZE, Duration.ofMillis(MAX_LOGS_TIMEOUT_MS))
        .flatMap(window -> window.reduce((o, o2) -> o + o2))
        .subscribeOn(Schedulers.elastic());
  }
}
